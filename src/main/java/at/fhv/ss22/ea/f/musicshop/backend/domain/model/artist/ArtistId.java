package at.fhv  .ss22.ea.f.musicshop.backend.domain.model.artist;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.bridges.ArtistIdBridge;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.FieldBridge;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class ArtistId implements Serializable {
    @Column
    @Type(type = "uuid-char")
    @FieldBridge(impl = ArtistIdBridge.class)
    private UUID artistId;

    public ArtistId(UUID id) {
        this.artistId = id;
    }

    protected ArtistId() {}

    public UUID getUUID() {
        return artistId;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistId artistId = (ArtistId) o;
        return Objects.equals(this.artistId, artistId.artistId);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(artistId);
    }
}
