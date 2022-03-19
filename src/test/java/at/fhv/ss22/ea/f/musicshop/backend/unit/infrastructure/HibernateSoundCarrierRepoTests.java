package at.fhv.ss22.ea.f.musicshop.backend.unit.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Song;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierType;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateSoundCarrierRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HibernateSoundCarrierRepoTests {

    private SoundCarrierRepository soundCarrierRepository = new HibernateSoundCarrierRepository();

    @Test
    void given_soundcarrier_when_searched_by_equal_but_not_same_id_then_carrier_found() {
        SoundCarrier carrier = SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 10, 5, "A-12", new ProductId(UUID.randomUUID()));
        soundCarrierRepository.add(carrier);
        SoundCarrierId surrogateId = new SoundCarrierId(carrier.getCarrierId().getUUID());

        //when
        Optional<SoundCarrier> carrierOpt = soundCarrierRepository.soundCarrierById(surrogateId);
        //then
        assertTrue(carrierOpt.isPresent());
        SoundCarrier s = carrierOpt.get();
        assertEquals(carrier.getCarrierId(), s.getCarrierId());
        assertEquals(carrier.getProductId(), s.getProductId());
    }

    @Test
    void given_invalid_product_id_when_searched_then_empty_result() {
        //given
        SoundCarrierId id = new SoundCarrierId(UUID.randomUUID());

        //when
        Optional<SoundCarrier> soundCarrierOptional = soundCarrierRepository.soundCarrierById(id);

        //then
        assertTrue(soundCarrierOptional.isEmpty());
    }
}
