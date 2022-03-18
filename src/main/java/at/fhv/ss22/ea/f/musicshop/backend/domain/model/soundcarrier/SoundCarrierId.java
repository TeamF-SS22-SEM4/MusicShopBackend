package at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier;

import java.util.Objects;
import java.util.UUID;

public class SoundCarrierId {
    private String id;

    public SoundCarrierId(String id) {
        this.id = id;
    }

    public String getUUID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoundCarrierId that = (SoundCarrierId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
