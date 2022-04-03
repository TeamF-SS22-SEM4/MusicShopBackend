package at.fhv.ss22.ea.f.musicshop.backend.infrastructure.bridges;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;
import org.hibernate.search.bridge.TwoWayStringBridge;

import java.util.UUID;

public class SessionIdBridge implements TwoWayStringBridge {
    @Override
    public Object stringToObject(String stringValue) {
        return new ArtistId(UUID.fromString(stringValue));
    }

    @Override
    public String objectToString(Object object) {
        SessionId sessionId = (SessionId) object;
        return sessionId.getValue();
    }
}
