package at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class CustomerId implements Serializable {
    @Column
    @Type(type = "uuid-char")
    private UUID customerId;

    public CustomerId(UUID id) {
        this.customerId = id;
    }

    @Generated
    protected CustomerId() {
    }

    public UUID getUUID() {
        return customerId;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerId that = (CustomerId) o;
        return Objects.equals(customerId, that.customerId);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
}
