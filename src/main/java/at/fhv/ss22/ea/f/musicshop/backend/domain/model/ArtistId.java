package at.fhv.ss22.ea.f.musicshop.backend.domain.model;

import java.util.UUID;

public class ArtistId {
    private UUID id;

    public ArtistId(UUID id) {
        this.id = id;
    }

    public UUID getUUID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArtistId artistId = (ArtistId) o;

        return id != null ? id.equals(artistId.id) : artistId.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
