package at.fhv.ss22.ea.f.musicshop.backend.domain.event;

import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchased;

// TODO: Move to repository
public interface EventPlacer {

    void placeProductPurchase(DigitalProductPurchased e);
}
