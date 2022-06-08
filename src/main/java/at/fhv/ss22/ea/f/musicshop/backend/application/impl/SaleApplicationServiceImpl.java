package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.*;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.CustomerApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.Logged;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RequiresRole;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.SessionKey;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rest.objects.OrderItem;
import at.fhv.ss22.ea.f.musicshop.backend.domain.event.EventPlacer;
import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchased;
import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchasedId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.Customer;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.Artist;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.CustomerId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.exceptions.SoundCarrierUnavailableException;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.Sale;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleItem;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierType;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.User;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.*;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

@Local(SaleApplicationService.class)
@Stateless
@Logged
public class SaleApplicationServiceImpl implements SaleApplicationService {

    @EJB private SoundCarrierRepository soundCarrierRepository;
    @EJB private SaleRepository saleRepository;
    @EJB private ProductRepository productRepository;
    @EJB private ArtistRepository artistRepository;
    @EJB private SessionRepository sessionRepository;
    @EJB private CustomerApplicationService customerApplicationService;

    @EJB private UserRepository userRepository;

    @EJB private EventPlacer eventPlacer;

    public SaleApplicationServiceImpl() {}

    public SaleApplicationServiceImpl(SessionRepository sessionRepository, SoundCarrierRepository soundCarrierRepository, SaleRepository saleRepository,
                                      ProductRepository productRepository, ArtistRepository artistRepository, EventPlacer eventPlacer, UserRepository userRepository, CustomerApplicationService customerApplicationService) {
        this.sessionRepository = sessionRepository;
        this.soundCarrierRepository = soundCarrierRepository;
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.artistRepository = artistRepository;
        this.customerApplicationService = customerApplicationService;
        this.eventPlacer = eventPlacer;
        this.userRepository = userRepository;
    }

    @Override
    @RequiresRole(UserRole.CUSTOMER)
    public String buyAsCustomer(@SessionKey String sessionId, List<OrderItem> orderItems,
                                String paymentMethod, String creditCardType, String creditCardNumber, String cvc) throws SessionExpired, NoPermissionForOperation, RemoteException, CarrierNotAvailableException, UnsupportedOperationException {
        Session session = sessionRepository.sessionById(new SessionId(sessionId)).orElseThrow(IllegalStateException::new);
        UUID customerId = session.getUserId().getUUID();
        if(paymentMethod.equals("Credit Card")) {
            CustomerDTO customer = customerApplicationService.customerById(sessionId, customerId);

            if(!customer.getCreditCardType().equals(creditCardType) ||
                    !customer.getCreditCardNumber().equals(creditCardNumber) ||
                    !customer.getCvc().equals(cvc)) {
                throw new IllegalArgumentException("Credit card information invalid");
            }
        }

        List<SoundCarrierAmountDTO> soundCarriers = new ArrayList<>();
        orderItems.forEach(orderItem ->
            soundCarriers.add(
                    SoundCarrierAmountDTO.builder()
                            .withCarrierId(orderItem.getCarrierId())
                            .withAmount(orderItem.getAmount())
                            .build()
            )
        );

        return buy(sessionId, soundCarriers, paymentMethod, customerId);
    }

    @Override
    //@RequiresRole(UserRole.EMPLOYEE) TODO: Add Customer Role
    public String buy(@SessionKey String sessionId, List<SoundCarrierAmountDTO> carrierAmounts, String paymentMethod, UUID customerId) throws CarrierNotAvailableException {
        EntityManagerUtil.beginTransaction();
        List<SaleItem> saleItems = new LinkedList<>();
        List<UUID> invalidCarriers = new LinkedList<>();
        for (SoundCarrierAmountDTO dto : carrierAmounts) {
            SoundCarrier carrier = soundCarrierRepository.soundCarrierById(new SoundCarrierId(dto.getCarrierId())).orElseThrow(IllegalStateException::new);

            try {
                saleItems.add(carrier.sell(dto.getAmount()));

                if(carrier.getType().equals(SoundCarrierType.DIGITAL)) {
                    Product product = productRepository.productById(carrier.getProductId()).orElseThrow(IllegalStateException::new);
                    User user = userRepository.userById(new UserId(customerId)).orElseThrow(IllegalStateException::new); // In this case customer = user

                    String artists = product.getArtistIds().stream()
                            .map(id -> artistRepository.artistById(id))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .map(Artist::getArtistName)
                            .collect(Collectors.joining(", "));

                    // Create new event
                    DigitalProductPurchased digitalProductPurchased = new DigitalProductPurchased(
                            new DigitalProductPurchasedId(UUID.randomUUID()),
                            user.getUsername(),
                            product.getProductId(),
                            artists
                    );
                    eventPlacer.placeProductPurchase(digitalProductPurchased);
                }
            } catch (SoundCarrierUnavailableException e) {
                invalidCarriers.add(carrier.getCarrierId().getUUID());
            }
        }

        if (!invalidCarriers.isEmpty()) {
            EntityManagerUtil.rollback();
            throw new CarrierNotAvailableException(invalidCarriers);
        }

        Session session = sessionRepository.sessionById(new SessionId(sessionId)).orElseThrow(IllegalStateException::new);
        long currentAmountOfSales = saleRepository.amountOfSales();
        Sale sale = Sale.newSale("R" + String.format("%06d", currentAmountOfSales + 1), saleItems, session.getUserId(), paymentMethod, new CustomerId(customerId));
        saleRepository.add(sale);
        EntityManagerUtil.commit();

        return sale.getInvoiceNumber();
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public SaleDTO saleByInvoiceNumber(@SessionKey String sessionId, String invoiceNumber) {
        return saleRepository.saleByInvoiceNumber(invoiceNumber).map(this::saleDtoFromSale).orElseThrow(NoSuchElementException::new);
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public void refund(@SessionKey String sessionId, String invoiceNumber, List<RefundedSaleItemDTO> refundedSaleItems) {
        EntityManagerUtil.beginTransaction();

        Sale sale = saleRepository.saleByInvoiceNumber(invoiceNumber).orElseThrow(NoSuchElementException::new);
        refundedSaleItems.forEach(refundedSaleItem -> {
            // Set matching saleItem isRefund to true
            SaleItem saleItem = sale.getSaleItemList().stream().filter(si ->
                    si.getCarrierId().getUUID().equals(refundedSaleItem.getSoundCarrierId())
            ).findFirst().orElseThrow(NoSuchElementException::new);

            saleItem.refund(refundedSaleItem.getAmountToRefund());

            SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierById(saleItem.getCarrierId()).orElseThrow(NoSuchElementException::new);
            soundCarrier.refund(refundedSaleItem.getAmountToRefund());
        });

        EntityManagerUtil.commit();
    }

    @RequiresRole(UserRole.CUSTOMER)
    @Override
    public List<SaleDTO> salesByCustomer(@SessionKey String sessionId) throws NoSuchElementException, SessionExpired {
        Session session = sessionRepository.sessionById(new SessionId(sessionId)).orElseThrow(IllegalStateException::new);
        User user = userRepository.userById(session.getUserId()).orElseThrow(NoSuchElementException::new);

        if (session.isExpired()) {
            throw new SessionExpired();
        }

        List<Sale> sales = saleRepository.salesByCustomerId(new CustomerId(user.getUserId().getUUID()));

        List<SaleDTO> saleDTOs = new ArrayList<>();
        for(Sale s : sales) {
            // TODO: Sort saleItems by productName and carrierType
            saleDTOs.add(saleDtoFromSale(s));
        }

        return saleDTOs;
    }

    private SaleDTO saleDtoFromSale(Sale sale) {
        List<SaleItemDTO> saleItemDTOs = new ArrayList<>();

        for (SaleItem saleItem : sale.getSaleItemList()) {
            SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierById(saleItem.getCarrierId()).orElseThrow(IllegalStateException::new);

            Product product = productRepository.productById(soundCarrier.getProductId()).orElseThrow(IllegalStateException::new);

            SaleItemDTO dto = SaleItemDTO.builder()
                    .withProductName(product.getName())
                    .withArtistName(product.getArtistIds().stream()
                            .map(artistId -> artistRepository.artistById(artistId))
                            .filter(Optional::isPresent)
                            .map(opt -> opt.get().getArtistName())
                            .collect(Collectors.joining(", ")))
                    .withSoundCarrierId(soundCarrier.getCarrierId().getUUID())
                    .withSoundCarrierName(soundCarrier.getType().getFriendlyName())
                    .withAmountOfCarriers(saleItem.getAmountOfCarriers())
                    .withPricePerCarrier(saleItem.getPricePerCarrier())
                    .withRefundedAmount(saleItem.getRefundedAmount())
                    .build();

            saleItemDTOs.add(dto);
        }

        return SaleDTO.builder()
                .withInvoiceNumber(sale.getInvoiceNumber())
                .withSaleItems(saleItemDTOs)
                .withTotalPrice(sale.getTotalPrice())
                .withDateOfSale(String.valueOf(sale.getTimeOfSale()))
                .build();
    }
}
