package at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
public class Employee {
    @EmbeddedId
    private EmployeeId employeeId;
    private String username;
    private String firstname;
    private String lastname;

    private LocalDateTime lastViewed;
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = UserRole.class)
    private List<UserRole> roles;
    @ElementCollection
    private List<SaleId> sales;
    @ElementCollection
    private List<String> subscribedTopics;

    public static Employee create(EmployeeId aEmployeeId, String aUsername, String aFirstname, String aLastname, List<UserRole> roles,List<SaleId> aSalesList) {
        return new Employee(aEmployeeId, aUsername, aFirstname, aLastname, roles, aSalesList);
    }

    public boolean hasRole(UserRole role) {
        return this.roles.contains(role);
    }

    public void updateLastViewed(LocalDateTime updatedLastViewed) {
        // New lastViewed can not be before old value
        if(updatedLastViewed.isBefore(this.lastViewed)) {
            throw new UnsupportedOperationException("New value can not be before old value.");
        }

        this.lastViewed = updatedLastViewed;
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
        this.subscribedTopics = new LinkedList<>();
        this.lastViewed = LocalDateTime.MIN;
    }

    public void subscribeTo(String topicName) {
        this.subscribedTopics.add(topicName);
    }
    public boolean unsubscribeFrom(String topicName) {
        return this.subscribedTopics.remove(topicName);
    }

    public List<String> getSubscribedTopics() {
        return Collections.unmodifiableList(subscribedTopics);
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

    public LocalDateTime getLastViewed() {
        return lastViewed;
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
