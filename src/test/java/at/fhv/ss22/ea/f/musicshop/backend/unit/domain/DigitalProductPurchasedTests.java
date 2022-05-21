package at.fhv.ss22.ea.f.musicshop.backend.unit.domain;

import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchased;
import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchasedId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DigitalProductPurchasedTests {

    @Test
    void given_purchaseeventdetails_when_creating_purchasevent_then_details_equals() {
        // given
        UUID eventIdUUID = UUID.randomUUID();
        DigitalProductPurchasedId digitalProductPurchasedIdExpected = new DigitalProductPurchasedId(eventIdUUID);
        UUID userIdUUID = UUID.randomUUID();
        UserId userIdExpected = new UserId(userIdUUID);
        UUID productIdUUID = UUID.randomUUID();
        ProductId productIdExpected = new ProductId(productIdUUID);

        // when
        DigitalProductPurchased digitalProductPurchased = new DigitalProductPurchased(
                digitalProductPurchasedIdExpected,
                userIdExpected,
                productIdExpected
        );

        // then
        assertEquals(digitalProductPurchasedIdExpected, digitalProductPurchased.getEventId());
        assertEquals(eventIdUUID, digitalProductPurchased.getEventId().getUUID());
        assertEquals(userIdExpected, digitalProductPurchased.getUserId());
        assertEquals(userIdUUID, digitalProductPurchased.getUserId().getUUID());
        assertEquals(productIdExpected, digitalProductPurchased.getProductId());
        assertEquals(productIdUUID, digitalProductPurchased.getProductId().getUUID());
    }
}
