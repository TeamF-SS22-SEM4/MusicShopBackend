package at.fhv.ss22.ea.f.musicshop.backend.unit.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.CustomerId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.Employee;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.Sale;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleItem;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EmployeeRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateEmployeeRepository;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HibernateEmployeeRepoTests {
    private EmployeeRepository employeeRepository = new HibernateEmployeeRepository();

    @Test
    void given_product_when_searched_by_equal_but_not_same_id_then_product_found() {
        Employee employee = Employee.create(new EmployeeId(UUID.randomUUID()), "userA", "max", "mustermann", List.of());
        employeeRepository.add(employee);
        EmployeeId surrogateId = new EmployeeId(employee.getEmployeeId().getUUID());

        //when
        Optional<Employee> employeeOpt = employeeRepository.employeeById(surrogateId);
        //then
        assertTrue(employeeOpt.isPresent());
        Employee e = employeeOpt.get();
        assertEquals(employee.getFirstname(), e.getFirstname());
        assertEquals(employee.getLastname(), e.getLastname());
        assertEquals(employee.getUsername(), e.getUsername());
    }

    @Test
    void given_invalid_product_id_when_searched_then_empty_result() {
        //given
        EmployeeId id = new EmployeeId(UUID.randomUUID());

        //when
        Optional<Employee> employeeOpt = employeeRepository.employeeById(id);

        //then
        assertTrue(employeeOpt.isEmpty());
    }
}