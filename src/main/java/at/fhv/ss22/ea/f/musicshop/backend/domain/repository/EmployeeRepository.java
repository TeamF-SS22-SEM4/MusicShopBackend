package at.fhv.ss22.ea.f.musicshop.backend.domain.repository;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.Employee;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;

import java.util.Optional;

public interface EmployeeRepository {

    void add(Employee employee);

    Optional<Employee> employeeById(EmployeeId employeeId);

    Optional<Employee> employeeByUserName(String username);
}
