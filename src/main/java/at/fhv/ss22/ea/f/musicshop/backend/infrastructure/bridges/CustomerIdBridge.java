package at.fhv.ss22.ea.f.musicshop.backend.infrastructure.bridges;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.CustomerId;
import org.hibernate.search.bridge.TwoWayStringBridge;

import java.util.UUID;

public class CustomerIdBridge implements TwoWayStringBridge {
    @Override
    public Object stringToObject(String stringValue) {
        return new CustomerId(UUID.fromString(stringValue));
    }

    @Override
    public String objectToString(Object object) {
        return ((CustomerId) object).getUUID().toString();
    }
}
