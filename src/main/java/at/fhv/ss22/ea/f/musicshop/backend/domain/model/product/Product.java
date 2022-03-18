package at.fhv.ss22.ea.f.musicshop.backend.domain.model.product;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;

import java.util.List;
import java.util.Objects;

public class Product {
    private ProductId id;
    private String name;
    private String releaseYear;
    private List<String> genre;
    private String label;
    private String duration;
    private List<ArtistId> artistIds;
    private List<Song> songs;

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
        return artistIds;
    }

    public List<Song> getSongs() {
        return songs;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(releaseYear, product.releaseYear) && Objects.equals(genre, product.genre) && Objects.equals(label, product.label) && Objects.equals(duration, product.duration) && Objects.equals(artistIds, product.artistIds) && Objects.equals(songs, product.songs);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(id, name, releaseYear, genre, label, duration, artistIds, songs);
    }
}