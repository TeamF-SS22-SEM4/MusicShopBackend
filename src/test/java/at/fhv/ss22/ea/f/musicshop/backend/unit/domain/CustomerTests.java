package at.fhv.ss22.ea.f.musicshop.backend.unit.domain;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.Address;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.Customer;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.CustomerId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerTests {
    @Test
    void given_customerdetails_when_creating_customer_then_details_equals() {
        // given
        UUID customerIdUUID = UUID.randomUUID();
        CustomerId customerIdExpected = new CustomerId(customerIdUUID);
        String firstnameExpected = "John";
        String lastnameExpected = "Doe";
        String emailExpected = "john.doe@tdd.at";
        String phoneNumberExpected = "+43 1212 12121212";
        Address addressExpected = new Address(
                "Street",
                "1",
                "Dornbirn",
                "6850",
                "Austria"
        );
        List<SaleId> purchasesExpected = new ArrayList<>(){
            {
                add(new SaleId(UUID.randomUUID()));
                add(new SaleId(UUID.randomUUID()));
                add(new SaleId(UUID.randomUUID()));
            }
        };

        // when
        Customer customer = Customer.create(
                customerIdExpected,
                firstnameExpected,
                lastnameExpected,
                emailExpected,
                phoneNumberExpected,
                addressExpected,
                purchasesExpected
        );

        // then
        assertEquals(customerIdExpected, customer.getId());
        assertEquals(customerIdUUID, customer.getId().getUUID());
        assertEquals(firstnameExpected, customer.getFirstname());
        assertEquals(lastnameExpected, customer.getLastname());
        assertEquals(emailExpected, customer.getEmail());
        assertEquals(phoneNumberExpected, customer.getPhoneNumber());
        assertEquals(addressExpected, customer.getAddress());
        assertEquals(addressExpected.getStreet(), customer.getAddress().getStreet());
        assertEquals(addressExpected.getHouseNumber(), customer.getAddress().getHouseNumber());
        assertEquals(addressExpected.getCity(), customer.getAddress().getCity());
        assertEquals(addressExpected.getPostalCode(), customer.getAddress().getPostalCode());
        assertEquals(addressExpected.getCountry(), customer.getAddress().getCountry());
        assertEquals(purchasesExpected.size(), customer.getPurchases().size());

        // Check content of lists
        for(int i = 0; i < purchasesExpected.size(); i++) {
            assertEquals(purchasesExpected.get(i), customer.getPurchases().get(i));
            assertEquals(purchasesExpected.get(i).getUUID(), customer.getPurchases().get(i).getUUID());
        }
    }
}
