package at.fhv.ss22.ea.f.musicshop.backend.domain.model.product;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class Product {

    @EmbeddedId
    private ProductId id;
    private String name;
    private String releaseYear;
    @ElementCollection
    private List<String> genre;
    private String label;
    private String duration;
    @ElementCollection
    private List<ArtistId> artistIds;
    @ElementCollection
    private List<Song> songs;

    public Product() {}

    public static Product create(ProductId aProductId, String aName, String aReleaseYear, List<String> aGenreList, String aLabel, String aDuration, List<ArtistId> aArtistIdList, List<Song> aSongList) {
        return new Product(aProductId, aName, aReleaseYear, aGenreList, aLabel, aDuration, aArtistIdList, aSongList);
    }

    private Product(ProductId aProductId, String aName, String aReleaseYear, List<String> aGenreList, String aLabel, String aDuration, List<ArtistId> aArtistIdList, List<Song> aSongList) {
        this.id = aProductId;
        this.name = aName;
        this.releaseYear = aReleaseYear;
        this.genre = aGenreList;
        this.label = aLabel;
        this.duration = aDuration;
        this.artistIds = aArtistIdList;
        this.songs = aSongList;
    }

    public ProductId getId() {
        return id;
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
        return Objects.equals(id, product.id);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}