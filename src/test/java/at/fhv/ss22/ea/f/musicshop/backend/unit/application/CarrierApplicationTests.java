package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SoundCarrierApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.exceptions.SoundCarrierUnavailableException;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierType;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SaleRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CarrierApplicationTests {

    //TODO test for: if sale fails, amount available is unchanged

    private SoundCarrierApplicationService soundCarrierApplicationService = InstanceProvider.getTestingSoundCarrierApplicationService();

    private SoundCarrierRepository soundCarrierRepository = InstanceProvider.getMockedSoundCarrierRepository();

    private SaleRepository saleRepository = InstanceProvider.getMockedSaleRepository();

    @Test
    void sell_carriers() throws CarrierNotAvailableException, SoundCarrierUnavailableException {
        //given
        List<SoundCarrier> carriers = List.of(
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 20, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 22, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 22, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.CD, 10, 5, "A1", new ProductId(UUID.randomUUID()))
        );
        for (SoundCarrier s : carriers) {
            when(soundCarrierRepository.soundCarrierById(s.getCarrierId())).thenReturn(Optional.of(s));
        }

        //when
        SoundCarrierAmountDTO buyingDTO = SoundCarrierAmountDTO.builder()
                            .withAmount(2)
                            .withCarrierId(carriers.get(0).getCarrierId().getUUID()).build();
        soundCarrierApplicationService.buy(List.of(buyingDTO), "CASH");

        //then
        assertEquals(3, carriers.get(0).getAmountInStore());
        verify(saleRepository).add(any());
    }

}
