package at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;

import java.util.Objects;
import java.util.UUID;

public class ArtistId {
    private UUID id;

    public ArtistId(UUID id) {
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
        ArtistId artistId = (ArtistId) o;
        return Objects.equals(id, artistId.id);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
