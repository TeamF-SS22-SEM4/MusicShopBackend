package at.fhv.ss22.ea.f.musicshop.backend.integration;

import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierType;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SaleRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationToInfrastructureTests {

    private SoundCarrierRepository soundCarrierRepository = InstanceProvider.getSoundCarrierRepository();

    private SaleApplicationService buyingApplicationService = InstanceProvider.getSoundCarrierApplicationService();

    private SaleRepository saleRepository = InstanceProvider.getSaleRepository();

    private AuthenticationApplicationService authenticationApplicationService = InstanceProvider.getMockedAuthenticationApplicationService();

    @BeforeAll
    void setup() throws SessionExpired {
        when(authenticationApplicationService.hasRole(any(), any())).thenReturn(true);
    }

    @Test
    void when_buying_too_many_sound_carriers_then_value_unchanged() throws NoPermissionForOperation {
        //given
        List<SoundCarrier> carriers = List.of(
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 20, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 22, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 22, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.CD, 10, 5, "A1", new ProductId(UUID.randomUUID()))
        );

        EntityManagerUtil.beginTransaction();
        for (SoundCarrier c: carriers) {
            soundCarrierRepository.add(c);
        }
        EntityManagerUtil.commit();

        //when
        SoundCarrierAmountDTO buyingDTO1 = SoundCarrierAmountDTO.builder()
                .withAmount(3)
                .withCarrierId(carriers.get(0).getCarrierId().getUUID()).build();
        SoundCarrierAmountDTO buyingDTO2 = SoundCarrierAmountDTO.builder()
                .withAmount(10)
                .withCarrierId(carriers.get(2).getCarrierId().getUUID()).build();

        //then
        try {
            buyingApplicationService.buy("placeholder", List.of(buyingDTO1, buyingDTO2), "CASH");
            fail(); // fails if no exception is thrown
        } catch(CarrierNotAvailableException e) {
            //then
            assertTrue(e.getUnavailableCarriers().contains(carriers.get(2).getCarrierId().getUUID()));
            assertFalse(e.getUnavailableCarriers().contains(carriers.get(0).getCarrierId().getUUID()));



            SoundCarrier carrierAct1 = soundCarrierRepository.soundCarrierById(carriers.get(0).getCarrierId()).orElseThrow();
            SoundCarrier carrierAct2 = soundCarrierRepository.soundCarrierById(carriers.get(2).getCarrierId()).orElseThrow();

            assertEquals(5, carrierAct1.getAmountInStore());
            assertEquals(5, carrierAct2.getAmountInStore());
        } catch (SessionExpired sessionExpired) {
            sessionExpired.printStackTrace();
        }
    }
}
