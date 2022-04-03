package at.fhv.ss22.ea.f.musicshop.backend.unit.domain;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.exceptions.SessionExpired;
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
        EmployeeId employeeId = new EmployeeId(UUID.randomUUID());
        Session session = Session.newForEmployee(employeeId);

        assertFalse(session.isExpired());
        assertNotNull(session.getSessionId().getValue());
        assertEquals(employeeId, session.getEmployeeId());
    }

    @Test
    void when_updating_valid_session_then_no_exception() {
        EmployeeId employeeId = new EmployeeId(UUID.randomUUID());
        Session session = Session.newForEmployee(employeeId);

        assertDoesNotThrow(session::refreshDuration);
    }

    @Test
    void when_updating_expired_session_then_exception() throws Exception {
        EmployeeId employeeId = new EmployeeId(UUID.randomUUID());
        Session session = Session.newForEmployee(employeeId);

        //make expired
        Field validUntil = Session.class.getDeclaredField("validUntil");
        validUntil.setAccessible(true);
        validUntil.set(session, Instant.now().minus(Duration.ofDays(1)));

        assertTrue(session.isExpired());

        assertThrows(SessionExpired.class, () ->
           session.refreshDuration()
        );
    }
}
