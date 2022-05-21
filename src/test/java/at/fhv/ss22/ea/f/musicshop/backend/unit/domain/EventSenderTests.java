package at.fhv.ss22.ea.f.musicshop.backend.unit.domain;

import at.fhv.ss22.ea.f.musicshop.backend.domain.event.EventSender;
import at.fhv.ss22.ea.f.musicshop.backend.domain.event.EventSenderImpl;
import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchased;
import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchasedId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EventRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventSenderTests {

    private EventSender eventSender;
    private EventRepository eventRepository = mock(EventRepository.class);

    @BeforeAll
    void setup() {
        this.eventSender = new EventSenderImpl(eventRepository);
    }

    @Test
    void when_event_in_repository_then_json_published_to_queue() {
        //given
        DigitalProductPurchased event = new DigitalProductPurchased(
                new DigitalProductPurchasedId(UUID.randomUUID()),
                new UserId(UUID.randomUUID()),
                "max mustermann",
                new ProductId(UUID.randomUUID()),
                "Some product",
                "30:00",
                new SoundCarrierId(UUID.randomUUID())
        );
        when(eventRepository.getNextOutgoing()).thenReturn(Optional.of(event));

        //when
        eventSender.sendDigitalPurchase();

    }

    @Test
    void when_no_event_in_repo_then_no_op() {
        //TODO
    }
}
