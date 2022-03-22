package at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class Employee {
    @EmbeddedId
    private EmployeeId employeeId;
    private String username;
    // password?
    private String firstname;
    private String lastname;
    @ElementCollection
    private List<SaleId> sales;

    public static Employee create(EmployeeId aEmployeeId, String aUsername, String aFirstname, String aLastname, List<SaleId> aSalesList) {
        return new Employee(aEmployeeId, aUsername, aFirstname, aLastname, aSalesList);
    }

    @Generated
    protected Employee() {}

    private Employee(EmployeeId aEmployeeId, String aUsername, String aFirstname, String aLastname, List<SaleId> aSalesList) {
        this.employeeId = aEmployeeId;
        this.username = aUsername;
        this.firstname = aFirstname;
        this.lastname = aLastname;
        this.sales = aSalesList;
    }

    public EmployeeId getEmployeeId() {
        return employeeId;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public List<SaleId> getSales() {
        return Collections.unmodifiableList(sales);
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(employeeId, employee.employeeId);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }
}
