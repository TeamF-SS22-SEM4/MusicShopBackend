package at.fhv.ss22.ea.f.musicshop.backend.domain.model;

import java.util.UUID;

public class SaleId {
    private UUID id;

    public SaleId(UUID id) {
        this.id = id;
    }

    public UUID getUUID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SaleId saleId = (SaleId) o;

        return id != null ? id.equals(saleId.id) : saleId.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
