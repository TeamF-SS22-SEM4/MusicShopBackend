package at.fhv.ss22.ea.f.musicshop.backend.domain.model.session;

import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.bridges.SessionIdBridge;
import org.hibernate.search.annotations.FieldBridge;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@Entity
public class Session {
    private static final int SESSION_ID_BYTE_LENGTH = 32;
    private static final Duration STANDARD_VALID_DURATION = Duration.ofMinutes(60);

    @EmbeddedId
    @FieldBridge(impl = SessionIdBridge.class)
    @AttributeOverride(name = "value", column = @Column(name = "sessionId"))
    private SessionId sessionId;
    @AttributeOverride(name = "userId", column = @Column(name = "userId"))
    private UserId userId;
    private Instant validUntil;

    private Session(SessionId sessionId, UserId userId, Instant validUntil) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.validUntil = validUntil;
    }

    @Generated
    protected Session() {}

    public static Session newForUser(UserId userId) {
        return new Session(newSessionId(), userId, Instant.now().plus(STANDARD_VALID_DURATION));
    }

    public boolean isExpired() {
        return this.validUntil.isBefore(Instant.now());
    }

    public void refreshDuration() throws SessionExpired {
        if (this.isExpired()) {
            throw new SessionExpired();
        }
        this.validUntil = this.validUntil.plus(STANDARD_VALID_DURATION);
    }

    private static SessionId newSessionId() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        while(sb.length() < SESSION_ID_BYTE_LENGTH){
            sb.append(Integer.toHexString(random.nextInt()));
        }
        return new SessionId(sb.substring(0, SESSION_ID_BYTE_LENGTH));
    }

    public SessionId getSessionId() {
        return sessionId;
    }

    public UserId getUserId() {
        return userId;
    }

    public Instant getValidUntil() {
        return validUntil;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(sessionId, session.sessionId);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(sessionId);
    }
}
