package at.fhv.ss22.ea.f.musicshop.backend.unit.domain;

import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {


    @Test
    void when_creating_new_then_isValid() {
        UserId userId = new UserId(UUID.randomUUID());
        Session session = Session.newForUser(userId);

        assertFalse(session.isExpired());
        assertNotNull(session.getSessionId().getValue());
        assertTrue(session.getValidUntil().isAfter(Instant.now()));
        assertEquals(userId, session.getUserId());
    }

    @Test
    void when_updating_valid_session_then_no_exception() {
        UserId userId = new UserId(UUID.randomUUID());
        Session session = Session.newForUser(userId);

        assertDoesNotThrow(session::refreshDuration);
    }

    @Test
    void when_updating_expired_session_then_exception() throws Exception {
        UserId userId = new UserId(UUID.randomUUID());
        Session session = Session.newForUser(userId);

        //make expired
        Field validUntil = Session.class.getDeclaredField("validUntil");
        validUntil.setAccessible(true);
        validUntil.set(session, Instant.now().minus(Duration.ofDays(1)));

        assertTrue(session.isExpired());

        assertThrows(SessionExpired.class, session::refreshDuration
        );
    }
}
