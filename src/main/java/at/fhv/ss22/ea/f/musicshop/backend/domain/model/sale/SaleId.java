package at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class SaleId implements Serializable {
    @Column
    @Type(type = "uuid-char")
    private UUID saleId;

    public SaleId(UUID id) {
        this.saleId = id;
    }
    protected SaleId() {}

    public UUID getUUID() {
        return saleId;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleId saleId = (SaleId) o;
        return Objects.equals(this.saleId, saleId.saleId);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(saleId);
    }
}
