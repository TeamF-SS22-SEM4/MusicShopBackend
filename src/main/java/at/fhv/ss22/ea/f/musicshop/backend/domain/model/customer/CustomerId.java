package at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer;

import java.util.Objects;

public class CustomerId {
    private String id;

    public CustomerId(String id) {
        this.id = id;
    }

    public String getUUID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerId that = (CustomerId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
