package at.fhv.ss22.ea.f.musicshop.backend.infrastructure.bridges;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import org.hibernate.search.bridge.TwoWayStringBridge;

import java.util.UUID;

public class ProductIdBridge implements TwoWayStringBridge {

    @Override
    public Object stringToObject(String stringValue) {
        return new ProductId(UUID.fromString(stringValue));
    }

    @Override
    public String objectToString(Object object) {
        ProductId productId = (ProductId) object;
        return productId.getUUID().toString();
    }
}
