package at.fhv.ss22.ea.f.musicshop.backend.domain.event;

import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchased;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EventRepository;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

// TODO: Move to infrastructure
@Local(EventPlacer.class)
@Stateless
public class EventPlacerImpl implements EventPlacer {

    @EJB
    EventRepository eventRepository;

    public EventPlacerImpl() {
    }

    @Override
    public void placeProductPurchase(DigitalProductPurchased e) {
        this.eventRepository.addOutgoingEvent(e);
    }
}
