package at.fhv.ss22.ea.f.musicshop.backend.domain.model;

import java.util.List;

public class Customer {
    private CustomerId id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private Address address;

    private List<SaleId> purchases;

}
