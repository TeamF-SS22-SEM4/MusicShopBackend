package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.communication.dto.CustomerDTO;
import at.fhv.ss22.ea.f.communication.dto.RefundedSaleItemDTO;
import at.fhv.ss22.ea.f.communication.dto.SaleDTO;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.CustomerApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.SaleApplicationServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rest.objects.OrderItem;
import at.fhv.ss22.ea.f.musicshop.backend.domain.event.EventPlacer;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.CustomerId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.User;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Song;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.Sale;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleItem;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierType;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SaleApplicationTests {

    private SaleApplicationServiceImpl saleApplicationService;

    private SoundCarrierRepository soundCarrierRepository = mock(SoundCarrierRepository.class);
    private SaleRepository saleRepository = mock(SaleRepository.class);
    private ProductRepository productRepository = mock(ProductRepository.class);
    private SessionRepository sessionRepository = mock(SessionRepository.class);
    private AuthenticationApplicationService authenticationApplicationService = mock(AuthenticationApplicationService.class);
    private ArtistRepository artistRepository = mock(ArtistRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private CustomerApplicationService customerApplicationService = mock(CustomerApplicationService.class);
    private EventPlacer eventPlacer = mock(EventPlacer.class);

    @BeforeAll
    void setup() throws SessionExpired {
        this.saleApplicationService = new SaleApplicationServiceImpl(sessionRepository, soundCarrierRepository, saleRepository, productRepository, artistRepository, eventPlacer, userRepository, customerApplicationService);
        when(authenticationApplicationService.hasRole(any(), any())).thenReturn(true);
    }

    @Test
    void sell_carriers() throws CarrierNotAvailableException, SessionExpired, NoPermissionForOperation {
        //given
        UUID customerIdExpected = UUID.randomUUID();
        List<SoundCarrier> carriers = List.of(
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 20, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 22, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 22, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.CD, 10, 5, "A1", new ProductId(UUID.randomUUID()))
        );
        for (SoundCarrier s : carriers) {
            when(soundCarrierRepository.soundCarrierById(s.getCarrierId())).thenReturn(Optional.of(s));
        }
        when(sessionRepository.sessionById(any())).thenReturn(Optional.of(Session.newForUser(new UserId(UUID.randomUUID()))));

        //when
        SoundCarrierAmountDTO buyingDTO = SoundCarrierAmountDTO.builder()
                            .withAmount(2)
                            .withCarrierId(carriers.get(0).getCarrierId().getUUID()).build();

        saleApplicationService.buy("placeholder", List.of(buyingDTO), "CASH", customerIdExpected);

        //then
        assertEquals(3, carriers.get(0).getAmountInStore());
        verify(saleRepository).add(any());
    }

    @Test
    void valid_buy_as_customer_with_cash() throws CarrierNotAvailableException, SessionExpired, NoPermissionForOperation, RemoteException {
        //given
        User user = User.create(
                new UserId(UUID.randomUUID()),
                "",
                "",
                "",
                List.of(),
                List.of()
        );
        Session session = Session.newForUser(user.getUserId());
        when(sessionRepository.sessionById(any())).thenReturn(Optional.of(session));
        SaleApplicationServiceImpl saleImpl = spy(saleApplicationService);
        doReturn("R0001").when(saleImpl).buy(any(), any(), anyString(), any());

        //when
        OrderItem orderItem = new OrderItem(UUID.randomUUID(), 1);
        List<OrderItem> orderItems = new LinkedList<>();
        orderItems.add(orderItem);
        saleImpl.buyAsCustomer(session.getSessionId().getValue(), orderItems, "Cash", null, null, null);
    }

    @Test
    void valid_buy_as_customer_with_credit_card() throws CarrierNotAvailableException, SessionExpired, NoPermissionForOperation, RemoteException {
        //given
        User user = User.create(
                new UserId(UUID.randomUUID()),
                "",
                "",
                "",
                List.of(),
                List.of()
        );
        Session session = Session.newForUser(user.getUserId());
        when(sessionRepository.sessionById(any())).thenReturn(Optional.of(session));
        SaleApplicationServiceImpl saleImpl = spy(saleApplicationService);
        doReturn("R0001").when(saleImpl).buy(any(), any(), anyString(), any());
        when(customerApplicationService.customerById(any(), any())).thenReturn(CustomerDTO.builder()
                        .id(UUID.randomUUID())
                        .creditCardNumber("some-number")
                        .creditCardType("mastercard")
                        .cvc("1234")
                .build());

        //when
        OrderItem orderItem = new OrderItem(UUID.randomUUID(), 1);
        List<OrderItem> orderItems = new LinkedList<>();
        orderItems.add(orderItem);
        saleImpl.buyAsCustomer(session.getSessionId().getValue(), orderItems, "Credit Card", "mastercard", "some-number", "1234");
    }

    @Test
    void valid_buy_as_customer_with_invalid_credit_card() throws CarrierNotAvailableException, SessionExpired, NoPermissionForOperation, RemoteException {
        //given
        User user = User.create(
                new UserId(UUID.randomUUID()),
                "",
                "",
                "",
                List.of(),
                List.of()
        );
        Session session = Session.newForUser(user.getUserId());
        when(sessionRepository.sessionById(any())).thenReturn(Optional.of(session));
        SaleApplicationServiceImpl saleImpl = spy(saleApplicationService);
        doReturn("R0001").when(saleImpl).buy(any(), any(), anyString(), any());
        when(customerApplicationService.customerById(any(), any())).thenReturn(CustomerDTO.builder()
                .id(UUID.randomUUID())
                .creditCardNumber("some-number")
                .creditCardType("mastercard")
                .cvc("1234")
                .build());

        //when
        OrderItem orderItem = new OrderItem(UUID.randomUUID(), 1);
        List<OrderItem> orderItems = new LinkedList<>();
        orderItems.add(orderItem);
        assertThrows(IllegalArgumentException.class, () -> saleImpl.buyAsCustomer(session.getSessionId().getValue(), orderItems, "Credit Card", "mastercard", "other-number", "1234"));
    }

    @Test
    void sell_digital_carriers() throws CarrierNotAvailableException, SessionExpired, NoPermissionForOperation {
        //given
        reset(saleRepository);
        User user = User.create(
                new UserId(UUID.randomUUID()),
                "max",
                "max",
                "mustermann",
                List.of(),
                List.of()
        );

        Product product = Product.create(
            new ProductId(UUID.randomUUID()),
                "some",
                "2000",
                List.of("Rock"),
                "someLabel",
                "30:00",
                List.of(new ArtistId(UUID.randomUUID())),
                List.of()
        );
        UUID customerIdExpected = UUID.randomUUID();
        List<SoundCarrier> carriers = List.of(
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.DIGITAL, 20, 1, "A1", product.getProductId())
        );
        when(productRepository.productById(product.getProductId())).thenReturn(Optional.of(product));
        when(userRepository.userById(user.getUserId())).thenReturn(Optional.of(user));

        for (SoundCarrier s : carriers) {
            when(soundCarrierRepository.soundCarrierById(s.getCarrierId())).thenReturn(Optional.of(s));
        }
        when(sessionRepository.sessionById(any())).thenReturn(Optional.of(Session.newForUser(new UserId(UUID.randomUUID()))));

        //when
        SoundCarrierAmountDTO buyingDTO = SoundCarrierAmountDTO.builder()
                .withAmount(1)
                .withCarrierId(carriers.get(0).getCarrierId().getUUID()).build();

        saleApplicationService.buy("placeholder", List.of(buyingDTO), "CASH", user.getUserId().getUUID());

        //then
        assertEquals(1, carriers.get(0).getAmountInStore());
        verify(saleRepository).add(any());
    }

    @Test
    void sell_carriers_with_too_high_amount() throws CarrierNotAvailableException, SessionExpired, NoPermissionForOperation {
        //given
        reset(saleRepository);
        UUID customerIdExpected = UUID.randomUUID();
        List<SoundCarrier> carriers = List.of(
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 20, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 22, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 22, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.CD, 10, 5, "A1", new ProductId(UUID.randomUUID()))
        );
        for (SoundCarrier s : carriers) {
            when(soundCarrierRepository.soundCarrierById(s.getCarrierId())).thenReturn(Optional.of(s));
        }
        when(sessionRepository.sessionById(any())).thenReturn(Optional.of(Session.newForUser(new UserId(UUID.randomUUID()))));

        //when
        SoundCarrierAmountDTO buyingDTO = SoundCarrierAmountDTO.builder()
                .withAmount(6)
                .withCarrierId(carriers.get(0).getCarrierId().getUUID()).build();

        assertThrows(CarrierNotAvailableException.class, () -> saleApplicationService.buy("placeholder", List.of(buyingDTO), "CASH", customerIdExpected));

        //then
        assertEquals(5, carriers.get(0).getAmountInStore());
        verify(saleRepository, never()).add(any());
    }
    //not testing for unchanged amountsInStore on failure because that mechanism relies on Transactional rollback
    // and that is not available when repositories are mocked
    // there is an integration test for that scenario

    @Test
    void given_invoiceNumber_when_saleByInvoiceNumber_then_return_matchingSale() throws SessionExpired, NoPermissionForOperation {
        // given
        UUID productIdUUID = UUID.randomUUID();
        ProductId productIdExpected = new ProductId(productIdUUID);
        String nameExpected = "SomeProduct";
        String releaseYearExpected = "2020";
        List<String> genresExpected = List.of("Rock", "Pop");
        String labelExpected = "TeamF";
        String durationExpected = "5:00";
        List<ArtistId> artistIdsExpected = List.of(new ArtistId(UUID.randomUUID()), new ArtistId(UUID.randomUUID()));
        List<Song> songsExpected = List.of(Song.create("Song 1", "3:00"), Song.create("Song 2", "2:00"));
        Product product = Product.create(productIdExpected, nameExpected, releaseYearExpected, genresExpected, labelExpected,
                durationExpected, artistIdsExpected, songsExpected);

        UUID soundCarrierIdUUID = UUID.randomUUID();
        SoundCarrierId soundCarrierIdExpected = new SoundCarrierId(soundCarrierIdUUID);
        SoundCarrierType soundCarrierTypeExpected = SoundCarrierType.CD;
        float priceExpected = 15;
        int amountInStoreExpected = 30;
        String locationExpected = "R001";
        SoundCarrier soundCarrier = SoundCarrier.create(soundCarrierIdExpected, soundCarrierTypeExpected, priceExpected,
                                                        amountInStoreExpected, locationExpected, productIdExpected);

        List<SaleItem> saleItemsExpected =  List.of(SaleItem.create(1, 10, soundCarrierIdExpected));
        String invoiceNumberExpected = "42";
        Sale sale = Sale.create(new SaleId(UUID.randomUUID()), invoiceNumberExpected, LocalDateTime.now(), "cash", new CustomerId(UUID.randomUUID()),saleItemsExpected, null);

        when(soundCarrierRepository.soundCarrierById(soundCarrierIdExpected)).thenReturn(Optional.of(soundCarrier));
        when(productRepository.productById(productIdExpected)).thenReturn(Optional.of(product));
        when(saleRepository.saleByInvoiceNumber(invoiceNumberExpected)).thenReturn(Optional.of(sale));

        // when
        SaleDTO saleActual = saleApplicationService.saleByInvoiceNumber("placeholder", invoiceNumberExpected);


        // then
        assertEquals(sale.getInvoiceNumber(), saleActual.getInvoiceNumber());
        assertEquals(sale.getSaleItemList().size(), saleActual.getSaleItems().size());
        assertEquals(sale.getTotalPrice(), saleActual.getTotalPrice());
    }

    @Test
    void given_invoiceNumber_and_refundedSaleItems_when_refund_then_amountsAsExpected() throws SessionExpired, NoPermissionForOperation {
        // given
        int carrierAmountAfterRefundExpected1 = 27;
        UUID soundCarrierIdExpected1 = UUID.randomUUID();
        SoundCarrier soundCarrier1 = SoundCarrier.create(
                new SoundCarrierId(soundCarrierIdExpected1),
                SoundCarrierType.CD,
                10,
                25,
                "R01",
                new ProductId(UUID.randomUUID())
        );

        int carrierAmountAfterRefundExpected2 = 5;
        UUID soundCarrierIdExpected2 = UUID.randomUUID();
        SoundCarrier soundCarrier2 = SoundCarrier.create(
                new SoundCarrierId(soundCarrierIdExpected2),
                SoundCarrierType.VINYL,
                20,
                5,
                "R01",
                new ProductId(UUID.randomUUID())
        );

        int carrierAmountAfterRefundExpected3 = 43;
        UUID soundCarrierIdExpected3 = UUID.randomUUID();
        SoundCarrier soundCarrier3 = SoundCarrier.create(
                new SoundCarrierId(soundCarrierIdExpected3),
                SoundCarrierType.CASSETTE,
                5,
                40,
                "R01",
                new ProductId(UUID.randomUUID())
        );

        int saleItemAmountAfterRefundExpected1 = 5;
        int saleItemRefundedAmountExpected1 = 2;
        SaleItem saleItem1 = SaleItem.ofCarrier(5, soundCarrier1);

        int saleItemAmountAfterRefundExpected2 = 1;
        int saleItemRefundedAmountExpected2 = 0;
        SaleItem saleItem2 = SaleItem.ofCarrier(1, soundCarrier2);

        int saleItemAmountAfterRefundExpected3 = 5;
        int saleItemRefundedAmountExpected3 = 3;
        SaleItem saleItem3 = SaleItem.ofCarrier(5, soundCarrier3);
        List<SaleItem> saleItems = List.of(saleItem1, saleItem2, saleItem3);

        String invoiceNumberExpected = "R00001";
        double totalPriceAfterRefundExpected = 95d;
        Sale sale = Sale.create(
                new SaleId(UUID.randomUUID()),
                invoiceNumberExpected,
                LocalDateTime.now(),
                "Cash",
                new CustomerId(UUID.randomUUID()),
                saleItems,
                new UserId(UUID.randomUUID())
        );

        List<RefundedSaleItemDTO> refundedSaleItems = new ArrayList<>();
        refundedSaleItems.add(
                RefundedSaleItemDTO.builder().
                        withSoundCarrierId(saleItem1.getCarrierId().getUUID())
                        .withAmountToRefund(saleItemRefundedAmountExpected1)
                        .build()
        );
        refundedSaleItems.add(
                RefundedSaleItemDTO.builder().
                        withSoundCarrierId(saleItem3.getCarrierId().getUUID())
                        .withAmountToRefund(saleItemRefundedAmountExpected3)
                        .build()
        );

        when(saleRepository.saleByInvoiceNumber(invoiceNumberExpected)).thenReturn(Optional.of(sale));
        when(soundCarrierRepository.soundCarrierById(soundCarrier1.getCarrierId())).thenReturn(Optional.of(soundCarrier1));
        when(soundCarrierRepository.soundCarrierById(soundCarrier2.getCarrierId())).thenReturn(Optional.of(soundCarrier2));
        when(soundCarrierRepository.soundCarrierById(soundCarrier3.getCarrierId())).thenReturn(Optional.of(soundCarrier3));

        // when
        saleApplicationService.refund("placeholder", invoiceNumberExpected, refundedSaleItems);


        // then
        assertEquals(totalPriceAfterRefundExpected, sale.getTotalPrice());
        assertEquals(carrierAmountAfterRefundExpected1, soundCarrier1.getAmountInStore());
        assertEquals(carrierAmountAfterRefundExpected2, soundCarrier2.getAmountInStore());
        assertEquals(carrierAmountAfterRefundExpected3, soundCarrier3.getAmountInStore());
        assertEquals(saleItemAmountAfterRefundExpected1, saleItem1.getAmountOfCarriers());
        assertEquals(saleItemAmountAfterRefundExpected2, saleItem2.getAmountOfCarriers());
        assertEquals(saleItemAmountAfterRefundExpected3, saleItem3.getAmountOfCarriers());
        assertEquals(saleItemRefundedAmountExpected1, saleItem1.getRefundedAmount());
        assertEquals(saleItemRefundedAmountExpected2, saleItem2.getRefundedAmount());
        assertEquals(saleItemRefundedAmountExpected3, saleItem3.getRefundedAmount());
    }

    @Test
    void given_customerId_when_saleByCustomerId_then_return_matching_sale() throws SessionExpired, NoPermissionForOperation {
        // given
        UUID productIdUUID = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        ProductId productIdExpected = new ProductId(productIdUUID);
        String nameExpected = "SomeProduct";
        String releaseYearExpected = "2020";
        List<String> genresExpected = List.of("Rock", "Pop");
        String labelExpected = "TeamF";
        String durationExpected = "5:00";
        List<ArtistId> artistIdsExpected = List.of(new ArtistId(UUID.randomUUID()), new ArtistId(UUID.randomUUID()));
        List<Song> songsExpected = List.of(Song.create("Song 1", "3:00"), Song.create("Song 2", "2:00"));
        Product product = Product.create(productIdExpected, nameExpected, releaseYearExpected, genresExpected, labelExpected,
                durationExpected, artistIdsExpected, songsExpected);

        UUID soundCarrierIdUUID = UUID.randomUUID();
        SoundCarrierId soundCarrierIdExpected = new SoundCarrierId(soundCarrierIdUUID);
        SoundCarrierType soundCarrierTypeExpected = SoundCarrierType.CD;
        float priceExpected = 15;
        int amountInStoreExpected = 30;
        String locationExpected = "R001";
        SoundCarrier soundCarrier = SoundCarrier.create(soundCarrierIdExpected, soundCarrierTypeExpected, priceExpected,
                amountInStoreExpected, locationExpected, productIdExpected);

        List<SaleItem> saleItemsExpected =  List.of(SaleItem.create(1, 10, soundCarrierIdExpected));
        String invoiceNumberExpected = "42";
        Sale sale = Sale.create(new SaleId(UUID.randomUUID()), invoiceNumberExpected, LocalDateTime.now(), "cash", new CustomerId(UUID.fromString(String.valueOf(customerId))),saleItemsExpected, null);

        User user = User.create(
                new UserId(customerId),
                "jdo007",
                "John",
                "Doe",
                List.of(UserRole.CUSTOMER),
                Collections.emptyList()
        );

        when(soundCarrierRepository.soundCarrierById(soundCarrierIdExpected)).thenReturn(Optional.of(soundCarrier));
        when(productRepository.productById(productIdExpected)).thenReturn(Optional.of(product));
        when(saleRepository.salesByCustomerId(new CustomerId(customerId))).thenReturn(List.of(sale));
        when(userRepository.userById(new UserId(customerId))).thenReturn(Optional.of(user));

        // when
        List<SaleDTO> saleListActual = saleApplicationService.salesByCustomerId("placeholder", customerId);
        SaleDTO saleActual = saleListActual.get(0);

        // then
        assertEquals(sale.getInvoiceNumber(), saleActual.getInvoiceNumber());
        assertEquals(sale.getSaleItemList().size(), saleActual.getSaleItems().size());
        assertEquals(sale.getTotalPrice(), saleActual.getTotalPrice());
    }
}
