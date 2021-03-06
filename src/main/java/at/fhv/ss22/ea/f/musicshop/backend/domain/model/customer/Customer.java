package at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class Customer {
    @EmbeddedId
    private CustomerId customerId;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private Address address;
    @ElementCollection
    private List<SaleId> purchases;

    public static Customer create(CustomerId aCustomerId, String aFirstname, String aLastname, String aEmail, String aPhoneNumber, Address aAddress, List<SaleId> aPurchases) {
        return new Customer(aCustomerId, aFirstname, aLastname, aEmail, aPhoneNumber, aAddress, aPurchases);
    }

    @Generated
    protected Customer() {
    }

    private Customer(CustomerId aCustomerId, String aFirstname, String aLastname, String aEmail, String aPhoneNumber, Address aAddress, List<SaleId> aPurchases) {
        this.customerId = aCustomerId;
        this.firstname = aFirstname;
        this.lastname = aLastname;
        this.email = aEmail;
        this.phoneNumber = aPhoneNumber;
        this.address = aAddress;
        this.purchases = aPurchases;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public List<SaleId> getPurchases() {
        return Collections.unmodifiableList(purchases);
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
}
