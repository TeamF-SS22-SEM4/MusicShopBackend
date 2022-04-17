package at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class Employee {
    @EmbeddedId
    private EmployeeId employeeId;
    private String username;
    private String firstname;
    private String lastname;
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = UserRole.class)
    private List<UserRole> roles;
    @ElementCollection
    private List<SaleId> sales;

    public static Employee create(EmployeeId aEmployeeId, String aUsername, String aFirstname, String aLastname, List<UserRole> roles,List<SaleId> aSalesList) {
        return new Employee(aEmployeeId, aUsername, aFirstname, aLastname, roles,aSalesList);
    }

    public boolean hasRole(UserRole role) {
        return this.roles.contains(role);
    }

    @Generated
    protected Employee() {
    }

    private Employee(EmployeeId employeeId, String username, String firstname, String lastname, List<UserRole> roles, List<SaleId> sales) {
        this.employeeId = employeeId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.roles = roles;
        this.sales = sales;
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

    public List<UserRole> getRoles() {
        return Collections.unmodifiableList(roles);
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
