package at.fhv.ss22.ea.f.musicshop.backend.domain.model.session;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SessionId implements Serializable {

    @Column
    private String value;

    public SessionId(String value) {
        this.value = value;
    }

    protected SessionId() {}

    public String getValue() {
        return value;
    }


    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionId sessionId = (SessionId) o;
        return Objects.equals(value, sessionId.value);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
