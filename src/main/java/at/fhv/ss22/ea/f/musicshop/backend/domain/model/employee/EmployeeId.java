package at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee;

import java.util.Objects;
import java.util.UUID;

public class EmployeeId {
    private String id;

    public EmployeeId(String id) {
        this.id = id;
    }

    public String getUUID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeId that = (EmployeeId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
