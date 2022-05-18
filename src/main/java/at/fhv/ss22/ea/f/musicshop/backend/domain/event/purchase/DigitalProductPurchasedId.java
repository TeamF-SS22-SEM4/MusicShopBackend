package at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase;

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
public class DigitalProductPurchasedId implements Serializable {
    @Column
    @Type(type = "uuid-char")
    @FieldBridge(impl = UUIDBridge.class)
    private UUID eventId;

    public DigitalProductPurchasedId(UUID eventId) {
        this.eventId = eventId;
    }

    @Generated
    protected DigitalProductPurchasedId() {

    }

    public UUID getUUID() {
        return eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DigitalProductPurchasedId that = (DigitalProductPurchasedId) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }
}
