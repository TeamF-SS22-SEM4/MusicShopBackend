package at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;

import java.util.List;
import java.util.Objects;

public class Employee {
    private EmployeeId id;
    private String username;
    // password?
    private String firstname;
    private String lastname;
    private List<SaleId> sales;

    public static Employee create(EmployeeId aEmployeeId, String aUsername, String aFirstname, String aLastname, List<SaleId> aSalesList) {
        return new Employee(aEmployeeId, aUsername, aFirstname, aLastname, aSalesList);
    }

    private Employee(EmployeeId aEmployeeId, String aUsername, String aFirstname, String aLastname, List<SaleId> aSalesList) {
        this.id = aEmployeeId;
        this.username = aUsername;
        this.firstname = aFirstname;
        this.lastname = aLastname;
        this.sales = aSalesList;
    }

    public EmployeeId getId() {
        return id;
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
        return sales;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(username, employee.username) && Objects.equals(firstname, employee.firstname) && Objects.equals(lastname, employee.lastname) && Objects.equals(sales, employee.sales);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstname, lastname, sales);
    }
}
