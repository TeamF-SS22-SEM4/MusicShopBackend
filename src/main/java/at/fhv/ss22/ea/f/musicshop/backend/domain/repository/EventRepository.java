package at.fhv.ss22.ea.f.musicshop.backend.domain.repository;

import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchased;

import java.util.Optional;

public interface EventRepository {

    void addOutgoingEvent(DigitalProductPurchased digitalProductPurchased);

    Optional<DigitalProductPurchased> getNextOutgoing();
}
