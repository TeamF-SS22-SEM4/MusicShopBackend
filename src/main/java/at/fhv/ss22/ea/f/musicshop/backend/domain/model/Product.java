package at.fhv.ss22.ea.f.musicshop.backend.domain.model;

import java.util.List;

public class Product {
    private ProductId id;
    private String name;
    private String releaseYear;
    private List<String> genre;
    private String label;
    private String duration;

    private List<ArtistId> artistIds;
    private List<Song> songs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id != null ? id.equals(product.id) : product.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}