package at.fhv.ss22.ea.f.musicshop.backend.unit.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchased;
import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchasedId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EventRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernateEventRepoTests {

    private EventRepository eventRepository = new HibernateEventRepository();

    @BeforeEach
    void beforeEach() {
        for (DigitalProductPurchased p : this.eventRepository.getNextOutgoings()) {
            this.eventRepository.remove(p);
        }
    }

    @Test
    void when_no_event_stored_then_no_next_outgoing() {
        //given - nothing
        //when
        List<DigitalProductPurchased> outgoings = eventRepository.getNextOutgoings();
        //then
        assertTrue(outgoings.isEmpty());
    }

    @Test
    void when_event_added_then_there_is_outgoing() {
        //given
        EntityManagerUtil.beginTransaction();
        DigitalProductPurchased event = new DigitalProductPurchased(
                new DigitalProductPurchasedId(UUID.randomUUID()),
                "maxMuster",
                new ProductId(UUID.randomUUID()),
                "some stuff"
        );
        this.eventRepository.addOutgoingEvent(event);

        EntityManagerUtil.commit();

        //when
        List<DigitalProductPurchased> outgoings = this.eventRepository.getNextOutgoings();

        //then
        assertNotNull(outgoings);
        assertEquals(1, outgoings.size());
        assertEquals(event.getEventId(), outgoings.get(0).getEventId());
    }
}
