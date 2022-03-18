package at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class SoundCarrierId implements Serializable {
    @Basic
    private UUID id;

    public SoundCarrierId(UUID id) {
        this.id = id;
    }
    protected SoundCarrierId() {}

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
