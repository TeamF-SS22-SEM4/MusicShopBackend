package at.fhv.ss22.ea.f.musicshop.backend.domain.repository;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;

import java.util.Optional;

public interface SoundCarrierRepository {

    void add(SoundCarrier soundCarrier);

    Optional<SoundCarrier> soundCarrierById(SoundCarrierId soundCarrierId);
}
