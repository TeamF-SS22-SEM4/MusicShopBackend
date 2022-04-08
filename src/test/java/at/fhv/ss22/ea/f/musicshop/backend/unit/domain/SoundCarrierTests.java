package at.fhv.ss22.ea.f.musicshop.backend.unit.domain;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SoundCarrierTests {
    @Test
    void given_soundcarrierdetails_when_creating_soundcarrier_then_details_equals() {
        // given
        UUID soundCarrierIdUUID = UUID.randomUUID();
        SoundCarrierId soundCarrierIdExpected = new SoundCarrierId(soundCarrierIdUUID);
        SoundCarrierType soundCarrierTypeExpected = SoundCarrierType.CD;
        float priceExpected = 15;
        int amountInStoreExpected = 30;
        String locationExpected = "R001";
        UUID productIdUUID = UUID.randomUUID();
        ProductId productIdExpected = new ProductId(productIdUUID);

        // when
        SoundCarrier soundCarrier = SoundCarrier.create(
                soundCarrierIdExpected,
                soundCarrierTypeExpected,
                priceExpected,
                amountInStoreExpected,
                locationExpected,
                productIdExpected
        );

        // then
        assertEquals(soundCarrierIdExpected, soundCarrier.getCarrierId());
        assertEquals(soundCarrierIdUUID, soundCarrier.getCarrierId().getUUID());
        assertEquals(soundCarrierTypeExpected, soundCarrier.getType());
        assertEquals(soundCarrierTypeExpected.getFriendlyName(), soundCarrier.getType().getFriendlyName());
        assertEquals(priceExpected, soundCarrier.getPrice());
        assertEquals(amountInStoreExpected, soundCarrier.getAmountInStore());
        assertEquals(locationExpected, soundCarrier.getLocation());
        assertEquals(productIdExpected, soundCarrier.getProductId());
        assertEquals(productIdUUID, soundCarrier.getProductId().getUUID());
    }
}
