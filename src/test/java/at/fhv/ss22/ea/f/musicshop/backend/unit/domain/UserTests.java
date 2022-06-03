package at.fhv.ss22.ea.f.musicshop.backend.unit.domain;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.User;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTests {

    @Test
    void employee_has_roles() {
        UUID employeeIdUUID = UUID.randomUUID();
        UserId userIdExpected = new UserId(employeeIdUUID);
        String usernameExpected = "john42";
        String firstnameExpected = "John";
        String lastNameExpected = "Doe";
        List<SaleId> salesExpected = new ArrayList<>(){
            {
                add(new SaleId(UUID.randomUUID()));
                add(new SaleId(UUID.randomUUID()));
                add(new SaleId(UUID.randomUUID()));
            }
        };

        // when
        User user = User.create(
                userIdExpected,
                usernameExpected,
                firstnameExpected,
                lastNameExpected,
                List.of(UserRole.fromName("employee"), UserRole.fromName("operator")),
                salesExpected
        );

        //then
        assertTrue(user.hasRole(UserRole.OPERATOR));
        assertFalse(user.hasRole(UserRole.ADMIN));
    }


    @Test
    void given_employeedetails_when_creating_employee_then_details_equals() {
        // given
        UUID employeeIdUUID = UUID.randomUUID();
        UserId userIdExpected = new UserId(employeeIdUUID);
        String usernameExpected = "john42";
        String firstnameExpected = "John";
        String lastNameExpected = "Doe";
        List<SaleId> salesExpected = new ArrayList<>(){
            {
                add(new SaleId(UUID.randomUUID()));
                add(new SaleId(UUID.randomUUID()));
                add(new SaleId(UUID.randomUUID()));
            }
        };

        // when
        User user = User.create(
                userIdExpected,
                usernameExpected,
                firstnameExpected,
                lastNameExpected,
                List.of(UserRole.EMPLOYEE),
                salesExpected
        );

        // then
        assertEquals(userIdExpected, user.getUserId());
        assertEquals(employeeIdUUID, user.getUserId().getUUID());
        assertEquals(usernameExpected, user.getUsername());
        assertEquals(firstnameExpected, user.getFirstname());
        assertEquals(lastNameExpected, user.getLastname());
        assertEquals(salesExpected.size(), user.getSales().size());

        // Check content of lists
        for(int i = 0; i < salesExpected.size(); i++) {
            assertEquals(salesExpected.get(i), user.getSales().get(i));
            assertEquals(salesExpected.get(i).getUUID(), user.getSales().get(i).getUUID());
        }
    }

    @Test
    void when_invalid_update_last_viewed_then_exception() {
        // given
        UUID employeeIdUUID = UUID.randomUUID();
        UserId userIdExpected = new UserId(employeeIdUUID);
        String usernameExpected = "john42";
        String firstnameExpected = "John";
        String lastNameExpected = "Doe";
        List<SaleId> salesExpected = new ArrayList<>(){
            {
                add(new SaleId(UUID.randomUUID()));
                add(new SaleId(UUID.randomUUID()));
                add(new SaleId(UUID.randomUUID()));
            }
        };
        User user = User.create(
                userIdExpected,
                usernameExpected,
                firstnameExpected,
                lastNameExpected,
                List.of(UserRole.EMPLOYEE),
                salesExpected
        );
        user.updateLastViewed(LocalDateTime.now());

        //when - then
        assertThrows(UnsupportedOperationException.class, () -> user.updateLastViewed(LocalDateTime.now().minusHours(1)));
    }
}
