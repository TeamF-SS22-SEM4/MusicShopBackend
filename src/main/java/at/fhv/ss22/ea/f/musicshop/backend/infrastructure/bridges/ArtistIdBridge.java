package at.fhv.ss22.ea.f.musicshop.backend.infrastructure.bridges;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import org.hibernate.search.bridge.TwoWayStringBridge;

import java.util.UUID;

public class ArtistIdBridge implements TwoWayStringBridge {
    @Override
    public Object stringToObject(String stringValue) {
        return new ArtistId(UUID.fromString(stringValue));
    }

    @Override
    public String objectToString(Object object) {
        ArtistId artistId = (ArtistId) object;
        return artistId.getUUID().toString();
    }
}
