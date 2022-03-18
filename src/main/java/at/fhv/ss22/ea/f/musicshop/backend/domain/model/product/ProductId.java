package at.fhv.ss22.ea.f.musicshop.backend.domain.model.product;

import java.util.Objects;
import java.util.UUID;

public class ProductId {
    private String id;

    public ProductId(String id) {
        this.id = id;
    }

    public String getUUID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductId productId = (ProductId) o;
        return Objects.equals(id, productId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
