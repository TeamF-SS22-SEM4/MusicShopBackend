package at.fhv.ss22.ea.f.musicshop.backend.domain.model.product;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;

import java.util.Objects;
import java.util.UUID;

public class ProductId {
    private UUID id;

    public ProductId(UUID id) {
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
        ProductId productId = (ProductId) o;
        return Objects.equals(id, productId.id);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
