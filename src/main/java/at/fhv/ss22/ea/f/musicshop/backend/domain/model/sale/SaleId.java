package at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class SaleId implements Serializable {
    private UUID id;

    public SaleId(UUID id) {
        this.id = id;
    }
    protected SaleId() {}

    public UUID getUUID() {
        return id;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleId saleId = (SaleId) o;
        return Objects.equals(id, saleId.id);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
