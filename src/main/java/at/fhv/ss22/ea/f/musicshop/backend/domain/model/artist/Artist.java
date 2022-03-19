package at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class Artist {
    @EmbeddedId
    private ArtistId artistId;
    private String artistName;
    private String countryOfOrigin;
    @ElementCollection
    private List<ProductId> productIds;

    public static Artist create(ArtistId anArtistId, String artistName, String aCountryOfOrigin, List<ProductId> aProductIds) {
        return new Artist(anArtistId, artistName, aCountryOfOrigin, aProductIds);
    }

    private Artist(ArtistId id, String artistName, String countryOfOrigin, List<ProductId> productIds) {
        this.artistId = id;
        this.artistName = artistName;
        this.countryOfOrigin = countryOfOrigin;
        this.productIds = productIds;
    }
    protected Artist() {}

    public ArtistId getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public List<ProductId> getProductIds() {
        return Collections.unmodifiableList(productIds);
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return Objects.equals(artistId, artist.artistId);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(artistId);
    }
}
