package at.fhv.ss22.ea.f.musicshop.backend.domain.model;

import java.util.List;

public class Artist {
    private ArtistId id;
    private String name;

    private List<ProductId> productIds;

    public ArtistId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ProductId> getProductIds() {
        return productIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artist artist = (Artist) o;

        return id != null ? id.equals(artist.id) : artist.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
