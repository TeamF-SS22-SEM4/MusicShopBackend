package at.fhv.ss22.ea.f.musicshop.backend.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer {
    private CustomerId id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private Address address;
    private List<SaleId> purchases;

    public static Customer create(CustomerId aCustomerId, String aFirstname, String aLastname, String aEmail, String aPhoneNumber, Address aAddress) {
        return new Customer(aCustomerId, aFirstname, aLastname, aEmail, aPhoneNumber, aAddress);
    }

    private Customer(CustomerId aCustomerId, String aFirstname, String aLastname, String aEmail, String aPhoneNumber, Address aAddress) {
        this.id = aCustomerId;
        this.firstname = aFirstname;
        this.lastname = aLastname;
        this.email = aEmail;
        this.phoneNumber = aPhoneNumber;
        this.address = aAddress;
        this.purchases = new ArrayList<>();
    }

    public CustomerId getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(firstname, customer.firstname) && Objects.equals(lastname, customer.lastname) && Objects.equals(email, customer.email) && Objects.equals(phoneNumber, customer.phoneNumber) && Objects.equals(address, customer.address) && Objects.equals(purchases, customer.purchases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email, phoneNumber, address, purchases);
    }
}
