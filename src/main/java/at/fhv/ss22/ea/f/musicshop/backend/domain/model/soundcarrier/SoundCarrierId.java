package at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;

import java.util.Objects;
import java.util.UUID;

public class SoundCarrierId {
    private UUID id;

    public SoundCarrierId(UUID id) {
        this.id = id;
    }

    public UUID getUUID() {
        return id;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoundCarrierId that = (SoundCarrierId) o;
        return Objects.equals(id, that.id);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
