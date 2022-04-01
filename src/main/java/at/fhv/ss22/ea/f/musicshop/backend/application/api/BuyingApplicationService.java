package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.communication.dto.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;

import java.util.List;
import java.util.UUID;

public interface BuyingApplicationService {

    //TODO add employeeId once client has logged in employee
    UUID buy(List<SoundCarrierAmountDTO> soundCarriers, String paymentMethod) throws CarrierNotAvailableException;
}
