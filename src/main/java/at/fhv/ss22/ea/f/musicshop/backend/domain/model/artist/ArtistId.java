package at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist;

import java.util.Objects;

public class ArtistId {
    private String id;

    public ArtistId(String id) {
        this.id = id;
    }

    public String getUUID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistId artistId = (ArtistId) o;
        return Objects.equals(id, artistId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
