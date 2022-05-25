package at.fhv.ss22.ea.f.musicshop.backend.unit.domain;

import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchased;
import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchasedId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
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
        String usernameExpected = "jdo1111";
        UUID productIdUUID = UUID.randomUUID();
        ProductId productIdExpected = new ProductId(productIdUUID);

        // when
        DigitalProductPurchased digitalProductPurchased = new DigitalProductPurchased(
                digitalProductPurchasedIdExpected,
                usernameExpected,
                productIdExpected
        );

        // then
        assertEquals(digitalProductPurchasedIdExpected, digitalProductPurchased.getEventId());
        assertEquals(eventIdUUID, digitalProductPurchased.getEventId().getUUID());
        assertEquals(usernameExpected, digitalProductPurchased.getUsername());
        assertEquals(productIdExpected, digitalProductPurchased.getProductId());
        assertEquals(productIdUUID, digitalProductPurchased.getProductId().getUUID());
    }
}
