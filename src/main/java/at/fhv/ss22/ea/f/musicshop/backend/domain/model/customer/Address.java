package at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Address {
    @Column
    private String street;
    private String houseNumber;
    private String city;
    private String postalCode;
    private String country;

    public Address(String street, String houseNumber, String city, String postalCode, String country) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }
    protected Address() {}

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (!Objects.equals(street, address.street)) return false;
        if (!Objects.equals(houseNumber, address.houseNumber)) return false;
        if (!Objects.equals(city, address.city)) return false;
        if (!Objects.equals(postalCode, address.postalCode)) return false;
        return Objects.equals(country, address.country);
    }

    @Generated
    @Override
    public int hashCode() {
        int result = street != null ? street.hashCode() : 0;
        result = 31 * result + (houseNumber != null ? houseNumber.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }
}
