package at.fhv.ss22.ea.f.musicshop.backend.domain.model;

import java.util.UUID;

public class SoundCarrierId {
    private UUID id;

    public SoundCarrierId(UUID id) {
        this.id = id;
    }

    public UUID getUUID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SoundCarrierId that = (SoundCarrierId) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
