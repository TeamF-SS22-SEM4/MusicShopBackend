package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import java.util.List;
import java.util.UUID;

public interface SoundCarrierApplicationService {

    //TODO add employeeId once client has logged in employee
    UUID buy(List<SoundCarrierAmountDTO> soundCarriers, String paymentMethod) throws CarrierNotAvailableException;
}
