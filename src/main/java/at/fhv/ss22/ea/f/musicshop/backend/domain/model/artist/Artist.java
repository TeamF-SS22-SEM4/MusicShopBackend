package at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.bridges.ArtistIdBridge;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.bridges.ProductIdBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Indexed
public class Artist {
    @EmbeddedId
    @FieldBridge(impl = ArtistIdBridge.class)
    private ArtistId artistId;
    @Field(termVector = TermVector.YES)
    private String artistName;
    private String countryOfOrigin;
    @ElementCollection
    @FieldBridge(impl = ProductIdBridge.class)
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

    @Generated
    protected Artist() {
    }

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
