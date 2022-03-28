package at.fhv.ss22.ea.f.musicshop.backend.domain.model.product;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.bridges.ArtistIdBridge;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.bridges.ProductIdBridge;
import org.hibernate.search.annotations.*;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Indexed
public class Product {
    @EmbeddedId
    @FieldBridge(impl = ProductIdBridge.class)
    private ProductId productId;
    @Field(termVector = TermVector.YES)
    private String name;
    @Field
    private String releaseYear;
    @ElementCollection
    @IndexedEmbedded
    private List<String> genre;
    @Field(termVector = TermVector.YES)
    private String label;
    private String duration;
    @ElementCollection
    @FieldBridge(impl = ArtistIdBridge.class)
    private List<ArtistId> artistIds;
    @ElementCollection
    @IndexedEmbedded
    private List<Song> songs;

    @Generated
    protected Product() {
    }

    public static Product create(ProductId aProductId, String aName, String aReleaseYear, List<String> aGenreList, String aLabel, String aDuration, List<ArtistId> aArtistIdList, List<Song> aSongList) {
        return new Product(aProductId, aName, aReleaseYear, aGenreList, aLabel, aDuration, aArtistIdList, aSongList);
    }

    private Product(ProductId aProductId, String aName, String aReleaseYear, List<String> aGenreList, String aLabel, String aDuration, List<ArtistId> aArtistIdList, List<Song> aSongList) {
        this.productId = aProductId;
        this.name = aName;
        this.releaseYear = aReleaseYear;
        this.genre = aGenreList;
        this.label = aLabel;
        this.duration = aDuration;
        this.artistIds = aArtistIdList;
        this.songs = aSongList;
    }

    public ProductId getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public List<String> getGenre() {
        return genre;
    }

    public String getLabel() {
        return label;
    }

    public String getDuration() {
        return duration;
    }

    public List<ArtistId> getArtistIds() {
        return Collections.unmodifiableList(artistIds);
    }

    public List<Song> getSongs() {
        return Collections.unmodifiableList(songs);
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}