package at.fhv.ss22.ea.f.musicshop.backend.integration;

import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SoundCarrierApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierType;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SaleRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;
import org.junit.jupiter.api.Test;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApplicationToInfrastructureTests {

    private SoundCarrierRepository soundCarrierRepository = InstanceProvider.getSoundCarrierRepository();

    private SoundCarrierApplicationService soundCarrierApplicationService = InstanceProvider.getSoundCarrierApplicationService();

    private SaleRepository saleRepository = InstanceProvider.getSaleRepository();

    @Test
    void when_buying_too_many_sound_carriers_then_value_unchanged() {
        //given
        List<SoundCarrier> carriers = List.of(
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 20, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 22, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 22, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.CD, 10, 5, "A1", new ProductId(UUID.randomUUID()))
        );

        for (SoundCarrier c: carriers) {
            soundCarrierRepository.add(c);
        }

        //when
        SoundCarrierAmountDTO buyingDTO1 = SoundCarrierAmountDTO.builder()
                .withAmount(3)
                .withCarrierId(carriers.get(0).getCarrierId().getUUID()).build();
        SoundCarrierAmountDTO buyingDTO2 = SoundCarrierAmountDTO.builder()
                .withAmount(10)
                .withCarrierId(carriers.get(2).getCarrierId().getUUID()).build();

        //then
        try {
            soundCarrierApplicationService.buy(List.of(buyingDTO1, buyingDTO2), "CASH");
            assertTrue(false); //statement ensures that test fails if no exception is thrown
        } catch(CarrierNotAvailableException e) {
            //then
            assertTrue(e.getUnavailableCarriers().contains(carriers.get(2).getCarrierId().getUUID()));
            assertFalse(e.getUnavailableCarriers().contains(carriers.get(0).getCarrierId().getUUID()));


            SoundCarrier carrierAct1 = soundCarrierRepository.soundCarrierById(carriers.get(0).getCarrierId()).get();
            SoundCarrier carrierAct2 = soundCarrierRepository.soundCarrierById(carriers.get(2).getCarrierId()).get();

            assertEquals(5, carrierAct1.getAmountInStore());
            assertEquals(5, carrierAct2.getAmountInStore());
        }
    }
}
