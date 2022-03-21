package at.fhv.ss22.ea.f.musicshop.backend.infrastructure.bridges;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import org.hibernate.search.bridge.TwoWayStringBridge;

import java.util.UUID;

public class SoundCarrierIdBridge implements TwoWayStringBridge {
    @Override
    public Object stringToObject(String stringValue) {
        return new SoundCarrierId(UUID.fromString(stringValue));
    }

    @Override
    public String objectToString(Object object) {
        return ((SoundCarrierId) object).getUUID().toString();
    }
}
