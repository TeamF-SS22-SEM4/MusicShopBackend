package at.fhv.ss22.ea.f.musicshop.backend.domain.model.user;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class UserId implements Serializable {
    @Column
    @Type(type = "uuid-char")
    private UUID userId;

    public UserId(UUID id) {
        this.userId = id;
    }

    @Generated
    protected UserId() {
    }

    public UUID getUUID() {
        return userId;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId that = (UserId) o;
        return Objects.equals(userId, that.userId);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
