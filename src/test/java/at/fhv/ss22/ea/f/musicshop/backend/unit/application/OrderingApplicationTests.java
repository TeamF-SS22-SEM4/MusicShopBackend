package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.communication.dto.DetailedOrderDTO;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierOrderDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.OrderingApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.OrderingApplicationServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.communication.jms.JMSClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierType;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.User;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.jms.JMSException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderingApplicationTests {

    private OrderingApplicationService orderingService;

    private JMSClient jmsClient = mock(JMSClient.class);
    private SoundCarrierRepository carrierRepository = mock(SoundCarrierRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private ProductRepository productRepository = mock(ProductRepository.class);
    private SessionRepository sessionRepository = mock(SessionRepository.class);

    @BeforeAll
    void setup() {
        this.orderingService = new OrderingApplicationServiceImpl(jmsClient, carrierRepository, userRepository, productRepository, sessionRepository);
    }

    @Test
    void when_placing_valid_order_then_order_message_published_and_true() throws Exception {
        //given
        Product product = Product.create(new ProductId(UUID.randomUUID()),
                "best album", "1969", List.of("Rock"), "misc", "13:13", List.of(new ArtistId(UUID.randomUUID())), List.of());
        User user = User.create(new UserId(UUID.randomUUID()), "test-user", "max", "mustermann", List.of(UserRole.EMPLOYEE), List.of());
        Session session = Session.newForUser(user.getUserId());
        SoundCarrier carrier = SoundCarrier.create(
                new SoundCarrierId(UUID.randomUUID()),
                SoundCarrierType.VINYL, 14, 10, "SOME", product.getProductId());
        SoundCarrierOrderDTO orderDTO = SoundCarrierOrderDTO.builder()
                .withAmount(5)
                .withCarrierId(carrier.getCarrierId().getUUID())
                .withOrderId(UUID.randomUUID())
                .build();
        when(sessionRepository.sessionById(session.getSessionId())).thenReturn(Optional.of(session));
        when(userRepository.userById(user.getUserId())).thenReturn(Optional.of(user));
        when(productRepository.productById(product.getProductId())).thenReturn(Optional.of(product));
        when(carrierRepository.soundCarrierById(carrier.getCarrierId())).thenReturn(Optional.of(carrier));


        //when
        boolean success = orderingService.placeOrder(session.getSessionId().getValue(), orderDTO);

        //then
        assertTrue(success);
        verify(jmsClient, times(1)).publishOrder(any(DetailedOrderDTO.class));
    }

    @Test
    void when_jms_exception_then_false() throws Exception {
        //given
        reset(jmsClient);
        Product product = Product.create(new ProductId(UUID.randomUUID()),
                "best album", "1969", List.of("Rock"), "misc", "13:13", List.of(new ArtistId(UUID.randomUUID())), List.of());
        User user = User.create(new UserId(UUID.randomUUID()), "test-user", "max", "mustermann", List.of(UserRole.EMPLOYEE), List.of());
        Session session = Session.newForUser(user.getUserId());
        SoundCarrier carrier = SoundCarrier.create(
                new SoundCarrierId(UUID.randomUUID()),
                SoundCarrierType.VINYL, 14, 10, "SOME", product.getProductId());
        SoundCarrierOrderDTO orderDTO = SoundCarrierOrderDTO.builder()
                .withAmount(5)
                .withCarrierId(carrier.getCarrierId().getUUID())
                .withOrderId(UUID.randomUUID())
                .build();
        when(sessionRepository.sessionById(session.getSessionId())).thenReturn(Optional.of(session));
        when(userRepository.userById(user.getUserId())).thenReturn(Optional.of(user));
        when(productRepository.productById(product.getProductId())).thenReturn(Optional.of(product));
        when(carrierRepository.soundCarrierById(carrier.getCarrierId())).thenReturn(Optional.of(carrier));
        doThrow(new JMSException("")).when(jmsClient).publishOrder(any());

        //when
        boolean success = orderingService.placeOrder(session.getSessionId().getValue(), orderDTO);

        //then
        assertFalse(success);
        verify(jmsClient, times(1)).publishOrder(any(DetailedOrderDTO.class));
    }

    @Test
    void when_approving_order_then_amount_changed() throws Exception {
        //given
        SoundCarrier carrier = SoundCarrier.create(
                new SoundCarrierId(UUID.randomUUID()),
                SoundCarrierType.VINYL, 14, 10, "SOME", new ProductId(UUID.randomUUID()));
        SoundCarrierOrderDTO orderDTO = SoundCarrierOrderDTO.builder()
                .withAmount(5)
                .withCarrierId(carrier.getCarrierId().getUUID())
                .withOrderId(UUID.randomUUID())
                .build();
        when(carrierRepository.soundCarrierById(carrier.getCarrierId())).thenReturn(Optional.of(carrier));

        //when
        boolean success = orderingService.approveOrder("placeholder", orderDTO);

        assertTrue(success);
        assertEquals(10 + 5, carrier.getAmountInStore());
    }

    @Test
    void when_invalid_carrier_id_in_approve_then_false() throws Exception {
        //given
        SoundCarrier carrier = SoundCarrier.create(
                new SoundCarrierId(UUID.randomUUID()),
                SoundCarrierType.VINYL, 14, 10, "SOME", new ProductId(UUID.randomUUID()));
        SoundCarrierOrderDTO orderDTO = SoundCarrierOrderDTO.builder()
                .withAmount(5)
                .withCarrierId(UUID.randomUUID())
                .withOrderId(UUID.randomUUID())
                .build();
        when(carrierRepository.soundCarrierById(any())).thenReturn(Optional.empty());

        //when
        boolean success = orderingService.approveOrder("placeholder", orderDTO);

        assertFalse(success);
    }


}
