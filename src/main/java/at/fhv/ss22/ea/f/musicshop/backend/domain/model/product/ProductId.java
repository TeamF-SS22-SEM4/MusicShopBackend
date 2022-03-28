package at.fhv.ss22.ea.f.musicshop.backend.domain.model.product;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.bridge.builtin.UUIDBridge;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class ProductId implements Serializable {
    @Column
    @Type(type = "uuid-char")
    @FieldBridge(impl = UUIDBridge.class)
    private UUID productId;

    public ProductId(UUID id) {
        this.productId = id;
    }

    @Generated
    protected ProductId() {
    }

    public UUID getUUID() {
        return productId;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductId productId = (ProductId) o;
        return Objects.equals(this.productId, productId.productId);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
