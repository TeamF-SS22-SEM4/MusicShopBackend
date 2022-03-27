package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.exceptions.SoundCarrierUnavailableException;

import java.util.List;

public interface SoundCarrierApplicationService {

    //TODO add employeeId once client has logged in employee
    void buy(List<SoundCarrierAmountDTO> soundCarriers, String paymentMethod) throws CarrierNotAvailableException, SoundCarrierUnavailableException;
}
