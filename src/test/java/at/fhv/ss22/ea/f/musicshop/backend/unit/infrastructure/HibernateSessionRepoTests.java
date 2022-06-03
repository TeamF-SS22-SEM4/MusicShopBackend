package at.fhv.ss22.ea.f.musicshop.backend.unit.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateSessionRepository;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HibernateSessionRepoTests {

    private SessionRepository sessionRepository = new HibernateSessionRepository();

    @Test
    void null_safety_when_getting_by_id() {
        assertTrue(sessionRepository.sessionById(null).isEmpty());
    }

    @Test
    void get_with_valid_id() {
        EntityManagerUtil.beginTransaction();
        //given

        UserId userId = new UserId(UUID.randomUUID());
        Session session = Session.newForUser(userId);
        SessionId surrogateId = new SessionId(session.getSessionId().getValue());

        sessionRepository.add(session);

        Session sessionAct = sessionRepository.sessionById(surrogateId).get();

        assertEquals(userId, sessionAct.getUserId());

        EntityManagerUtil.rollback();
    }

    @Test
    void get_with_invalid_id() {
        SessionId sessionId = new SessionId("invalid");

        assertTrue(sessionRepository.sessionById(sessionId).isEmpty());
    }

    @Test
    void delete_expired_session() throws NoSuchFieldException, IllegalAccessException {
        EntityManagerUtil.beginTransaction();
        //given
        Session validSession = Session.newForUser(new UserId(UUID.randomUUID()));
        Session invalidSession = Session.newForUser(new UserId(UUID.randomUUID()));
        Field validUntil = Session.class.getDeclaredField("validUntil");
        validUntil.setAccessible(true);
        validUntil.set(invalidSession, Instant.now().minus(Duration.ofDays(1)));
        sessionRepository.add(validSession);
        sessionRepository.add(invalidSession);

        //when
        sessionRepository.removeExpiredSessions();

        //then
        assertTrue(sessionRepository.sessionById(validSession.getSessionId()).isPresent());
        assertTrue(sessionRepository.sessionById(invalidSession.getSessionId()).isEmpty());
        EntityManagerUtil.rollback();
    }

}
