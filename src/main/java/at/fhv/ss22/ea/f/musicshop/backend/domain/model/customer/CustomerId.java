package at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;

import java.util.Objects;
import java.util.UUID;

public class CustomerId {
    private UUID id;

    public CustomerId(UUID id) {
        this.id = id;
    }

    public UUID getUUID() {
        return id;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerId that = (CustomerId) o;
        return Objects.equals(id, that.id);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
