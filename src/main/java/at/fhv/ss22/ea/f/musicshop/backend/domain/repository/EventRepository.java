package at.fhv.ss22.ea.f.musicshop.backend.domain.repository;

import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchased;

import java.util.List;

public interface EventRepository {

    void addOutgoingEvent(DigitalProductPurchased digitalProductPurchased);

    List<DigitalProductPurchased> getNextOutgoings();

    void remove(DigitalProductPurchased event);
}
