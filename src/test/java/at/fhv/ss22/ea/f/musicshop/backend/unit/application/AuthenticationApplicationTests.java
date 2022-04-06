package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.communication.dto.LoginResultDTO;
import at.fhv.ss22.ea.f.communication.exception.AuthenticationFailed;
import at.fhv.ss22.ea.f.musicshop.backend.communication.authentication.LdapClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.Employee;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EmployeeRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationApplicationTests {

    private AuthenticationApplicationService authenticationApplicationService = InstanceProvider.getTestingAuthenticationApplicationService();

    private LdapClient ldapClient = InstanceProvider.getMockedLdapClient();

    private EmployeeRepository employeeRepository = InstanceProvider.getMockedEmployeeRepository();

    private SessionRepository sessionRepository = InstanceProvider.getMockedSessionRepository();

    @Test
    void basic_login() throws AuthenticationFailed {
        //given
        Employee employee = Employee.create(new EmployeeId(UUID.randomUUID()), "userA", "max", "mustermann", List.of(UserRole.EMPLOYEE, UserRole.OPERATOR),List.of());
        when(ldapClient.credentialsValid(any(), any())).thenReturn(true);
        when(employeeRepository.employeeByUserName(anyString())).thenReturn(Optional.of(employee));

        //when
        LoginResultDTO loginResultDTO = authenticationApplicationService.login("lukas1", "password");

        //then
        verify(sessionRepository, times(1)).add(any());
    }

    @Test
    void roles_of_valid_session() throws SessionExpired {
        //given
        Employee employee = Employee.create(
                new EmployeeId(UUID.randomUUID()),
                "buttsoup-barnes",
                "Buut",
                "Soup",
                List.of(UserRole.EMPLOYEE),
                List.of()
        );
        Session session = Session.newForEmployee(employee.getEmployeeId());
        when(sessionRepository.sessionById(session.getSessionId())).thenReturn(Optional.of(session));
        when(employeeRepository.employeeById(employee.getEmployeeId())).thenReturn(Optional.of(employee));

        //when
        boolean resultOperator = authenticationApplicationService.hasRole(session.getSessionId(), UserRole.OPERATOR);
        boolean resultEmployee = authenticationApplicationService.hasRole(session.getSessionId(), UserRole.EMPLOYEE);

        //then
        assertTrue(resultEmployee);
        assertFalse(resultOperator);
    }

    @Test
    void roles_of_invalid_session() throws NoSuchFieldException, IllegalAccessException {
        Session session = Session.newForEmployee(new EmployeeId(UUID.randomUUID()));
        //make expired
        Field validUntil = Session.class.getDeclaredField("validUntil");
        validUntil.setAccessible(true);
        validUntil.set(session, Instant.now().minus(Duration.ofDays(1)));

        when(sessionRepository.sessionById(session.getSessionId())).thenReturn(Optional.of(session));

        assertThrows(SessionExpired.class, () -> authenticationApplicationService.hasRole(session.getSessionId(), UserRole.EMPLOYEE));
        verify(sessionRepository, times(1)).removeExpiredSessions();
    }
}
