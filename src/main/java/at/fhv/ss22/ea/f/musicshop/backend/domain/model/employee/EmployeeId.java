package at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class EmployeeId implements Serializable {
    @Column
    @Type(type = "uuid-char")
    private UUID employeeId;

    public EmployeeId(UUID id) {
        this.employeeId = id;
    }
    protected EmployeeId() {}

    public UUID getUUID() {
        return employeeId;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeId that = (EmployeeId) o;
        return Objects.equals(employeeId, that.employeeId);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }
}
