package at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class SoundCarrierId implements Serializable {
    @Column
    @Type(type = "uuid-char")
    private UUID carrierId;

    public SoundCarrierId(UUID id) {
        this.carrierId = id;
    }

    @Generated
    protected SoundCarrierId() {
    }

    public UUID getUUID() {
        return carrierId;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoundCarrierId that = (SoundCarrierId) o;
        return Objects.equals(carrierId, that.carrierId);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(carrierId);
    }
}
