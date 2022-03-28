package at.fhv.ss22.ea.f.musicshop.backend.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.Employee;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EmployeeRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class HibernateEmployeeRepository implements EmployeeRepository {

    private EntityManager em;

    public HibernateEmployeeRepository() {
        this.em = EntityManagerUtil.getEntityManager();
    }

    @Override
    public void add(Employee employee) {
        this.em.getTransaction().begin();
        this.em.persist(employee);
        this.em.getTransaction().commit();
    }

    @Override
    public Optional<Employee> employeeById(EmployeeId employeeId) {
        TypedQuery<Employee> query = this.em.createQuery(
                "select e from Employee e where e.employeeId = :employee_id",
                Employee.class
        );
        query.setParameter("employee_id", employeeId);
        return query.getResultStream().findFirst();
    }
}
