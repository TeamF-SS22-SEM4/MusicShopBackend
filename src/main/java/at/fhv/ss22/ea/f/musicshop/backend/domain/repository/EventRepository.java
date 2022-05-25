package at.fhv.ss22.ea.f.musicshop.backend.domain.repository;

import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchased;

import java.util.List;
import java.util.Optional;

public interface EventRepository {

    void addOutgoingEvent(DigitalProductPurchased digitalProductPurchased);

    List<DigitalProductPurchased> getNextOutgoing();

    void remove(DigitalProductPurchased event);
}
