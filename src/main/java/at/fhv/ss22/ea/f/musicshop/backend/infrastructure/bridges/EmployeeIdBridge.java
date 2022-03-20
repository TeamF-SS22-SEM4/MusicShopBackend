package at.fhv.ss22.ea.f.musicshop.backend.infrastructure.bridges;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;
import org.hibernate.search.bridge.TwoWayStringBridge;

import java.util.UUID;

public class EmployeeIdBridge implements TwoWayStringBridge {
    @Override
    public Object stringToObject(String stringValue) {
        return new EmployeeId(UUID.fromString(stringValue));
    }

    @Override
    public String objectToString(Object object) {
        return ((EmployeeId) object).getUUID().toString();
    }
}
