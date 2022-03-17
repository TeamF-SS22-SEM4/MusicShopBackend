package at.fhv.ss22.ea.f.musicshop.backend.domain.model;

import java.util.List;
import java.util.Objects;

public class Artist {
    private ArtistId id;
    // Firstname und Lastname als Value Object FullName?
    private String firstname;
    private String lastname;

    private List<ProductId> productIds;


    public ArtistId getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public List<ProductId> getProductIds() {
        return productIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return Objects.equals(id, artist.id) && Objects.equals(firstname, artist.firstname) && Objects.equals(lastname, artist.lastname) && Objects.equals(productIds, artist.productIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, productIds);
    }
}
