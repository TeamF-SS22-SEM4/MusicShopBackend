package at.fhv.ss22.ea.f.musicshop.backend.infrastructure.bridges;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;
import org.hibernate.search.bridge.TwoWayStringBridge;

import java.util.UUID;

public class SaleIdBridge implements TwoWayStringBridge {

    @Override
    public Object stringToObject(String stringValue) {
        return new SaleId(UUID.fromString(stringValue));
    }

    @Override
    public String objectToString(Object object) {
        return ((SaleId) object).getUUID().toString();
    }
}
