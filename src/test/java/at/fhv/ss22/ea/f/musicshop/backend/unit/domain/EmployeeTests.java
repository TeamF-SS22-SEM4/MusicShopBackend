package at.fhv.ss22.ea.f.musicshop.backend.unit.domain;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.Employee;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTests {

    @Test
    void employee_has_roles() {
        UUID employeeIdUUID = UUID.randomUUID();
        EmployeeId employeeIdExpected = new EmployeeId(employeeIdUUID);
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
        Employee employee = Employee.create(
                employeeIdExpected,
                usernameExpected,
                firstnameExpected,
                lastNameExpected,
                List.of(UserRole.EMPLOYEE, UserRole.OPERATOR),
                salesExpected
        );

        //then
        assertTrue(employee.hasRole(UserRole.OPERATOR));
        assertFalse(employee.hasRole(UserRole.ADMIN));
    }


    @Test
    void given_employeedetails_when_creating_employee_then_details_equals() {
        // given
        UUID employeeIdUUID = UUID.randomUUID();
        EmployeeId employeeIdExpected = new EmployeeId(employeeIdUUID);
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
        Employee employee = Employee.create(
                employeeIdExpected,
                usernameExpected,
                firstnameExpected,
                lastNameExpected,
                List.of(UserRole.EMPLOYEE),
                salesExpected
        );

        // then
        assertEquals(employeeIdExpected, employee.getEmployeeId());
        assertEquals(employeeIdUUID, employee.getEmployeeId().getUUID());
        assertEquals(usernameExpected, employee.getUsername());
        assertEquals(firstnameExpected, employee.getFirstname());
        assertEquals(lastNameExpected, employee.getLastname());
        assertEquals(salesExpected.size(), employee.getSales().size());

        // Check content of lists
        for(int i = 0; i < salesExpected.size(); i++) {
            assertEquals(salesExpected.get(i), employee.getSales().get(i));
            assertEquals(salesExpected.get(i).getUUID(), employee.getSales().get(i).getUUID());
        }
    }
}
