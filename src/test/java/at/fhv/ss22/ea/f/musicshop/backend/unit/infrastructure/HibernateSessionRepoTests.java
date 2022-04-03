package at.fhv.ss22.ea.f.musicshop.backend.unit.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HibernateSessionRepoTests {

    private SessionRepository sessionRepository = InstanceProvider.getSessionRepository();

    @Test
    void get_with_valid_id() {
        EntityManagerUtil.beginTransaction();
        //given

        EmployeeId employeeId = new EmployeeId(UUID.randomUUID());
        Session session = Session.newForEmployee(employeeId);
        SessionId surrogateId = new SessionId(session.getSessionId().getValue());

        sessionRepository.add(session);

        Session sessionAct = sessionRepository.sessionById(surrogateId).get();

        assertEquals(employeeId, sessionAct.getEmployeeId());

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
        Session validSession = Session.newForEmployee(new EmployeeId(UUID.randomUUID()));
        Session invalidSession = Session.newForEmployee(new EmployeeId(UUID.randomUUID()));
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
