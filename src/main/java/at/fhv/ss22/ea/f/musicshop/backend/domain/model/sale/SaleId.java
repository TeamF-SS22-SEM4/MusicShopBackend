package at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale;

import java.util.Objects;
import java.util.UUID;

public class SaleId {
    private String id;

    public SaleId(String id) {
        this.id = id;
    }

    public String getUUID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleId saleId = (SaleId) o;
        return Objects.equals(id, saleId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
