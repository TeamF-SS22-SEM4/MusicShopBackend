package at.fhv.ss22.ea.f.musicshop.backend.unit.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Song;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierType;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HibernateSoundCarrierRepoTests {

    private SoundCarrierRepository soundCarrierRepository = InstanceProvider.getSoundCarrierRepository();

    @Test
    void given_soundcarrier_when_searched_by_equal_but_not_same_id_then_carrier_found() {
        SoundCarrier carrier = SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 10, 5, "A-12", new ProductId(UUID.randomUUID()));
        EntityManagerUtil.beginTransaction();
        soundCarrierRepository.add(carrier);
        EntityManagerUtil.commit();
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
    void given_invalid_carrier_id_when_searched_then_empty_result() {
        //given
        SoundCarrierId id = new SoundCarrierId(UUID.randomUUID());

        //when
        Optional<SoundCarrier> soundCarrierOptional = soundCarrierRepository.soundCarrierById(id);

        //then
        assertTrue(soundCarrierOptional.isEmpty());
    }

    @Test
    void get_carriers_by_product_id() {
        //given
        Product product1 = Product.create(
                new ProductId(UUID.randomUUID()),
                "some",
                "2020",
                List.of("pop", "rock"),
                "best label",
                "40:00",
                List.of(new ArtistId(UUID.randomUUID())),
                List.of(Song.create("songA", "3:00"), Song.create("SongB", "3:00"))
        );
        Product product2 = Product.create(
                new ProductId(UUID.randomUUID()),
                "another one",
                "1999",
                List.of("K-Pop"),
                "not so good label",
                "12:00",
                List.of(new ArtistId(UUID.randomUUID())),
                List.of(Song.create("some song", "5:00"))
        );
        List<SoundCarrier> carriers = List.of(
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 15f, 5, "A5", product1.getProductId()),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.CD, 10f, 6, "A5", product1.getProductId()),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 15f, 5, "A5", product2.getProductId()),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.CD, 10f, 6, "A5", product2.getProductId())
        );
        EntityManagerUtil.beginTransaction();
        for (SoundCarrier s: carriers) {
            soundCarrierRepository.add(s);
        }
        EntityManagerUtil.commit();

        //when
        List<SoundCarrier> carriersActual = soundCarrierRepository.soundCarriersByProductId(product1.getProductId());

        //then
        assertTrue(carriersActual.contains(carriers.get(0)));
        assertTrue(carriersActual.contains(carriers.get(1)));
        assertFalse(carriersActual.contains(carriers.get(2)));
        assertFalse(carriersActual.contains(carriers.get(3)));
    }
}
