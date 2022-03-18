package at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;

import java.util.List;
import java.util.Objects;

public class Artist {
    private ArtistId id;
    // Firstname und Lastname als Value Object FullName?
    private String firstname;
    private String lastname;
    private String countryOfOrigin;
    private List<ProductId> productIds;

    public static Artist create(ArtistId anArtistId, String aFirstname, String aLastname, String aCountryOfOrigin, List<ProductId> aProductIds) {
        return new Artist(anArtistId, aFirstname, aLastname, aCountryOfOrigin, aProductIds);
    }

    private Artist(ArtistId id, String firstname, String lastname, String countryOfOrigin, List<ProductId> productIds) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.countryOfOrigin = countryOfOrigin;
        this.productIds = productIds;
    }

    public ArtistId getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public List<ProductId> getProductIds() {
        return productIds;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return Objects.equals(id, artist.id) && Objects.equals(firstname, artist.firstname) && Objects.equals(lastname, artist.lastname) && Objects.equals(countryOfOrigin, artist.countryOfOrigin) && Objects.equals(productIds, artist.productIds);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, countryOfOrigin, productIds);
    }
}
