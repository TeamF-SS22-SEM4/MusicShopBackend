package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import java.util.List;
import java.util.UUID;

public class CarrierNotAvailableException extends Exception {
    private List<UUID> unavailableCarriers;

    public CarrierNotAvailableException(List<UUID> unavailableCarriers) {
        this.unavailableCarriers = unavailableCarriers;
    }

    public List<UUID> getUnavailableCarriers() {
        return unavailableCarriers;
    }
}
