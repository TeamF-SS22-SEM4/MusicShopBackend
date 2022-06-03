package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.communication.dto.LoginResultDTO;
import at.fhv.ss22.ea.f.communication.exception.AuthenticationFailed;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.AuthenticationApplicationServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.communication.authentication.LdapClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.User;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationApplicationTests {

    private AuthenticationApplicationService authenticationApplicationService;

    private LdapClient ldapClient = mock(LdapClient.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private SessionRepository sessionRepository = mock(SessionRepository.class);

    @BeforeAll
    void setup() {
        this.authenticationApplicationService = new AuthenticationApplicationServiceImpl(ldapClient, sessionRepository, userRepository);
    }

    @Test
    void basic_login() throws AuthenticationFailed {
        //given
        reset(sessionRepository);
        User user = User.create(new UserId(UUID.randomUUID()), "userA", "max", "mustermann", List.of(UserRole.EMPLOYEE, UserRole.OPERATOR),List.of());
        when(ldapClient.employeeCredentialsValid(any(), any())).thenReturn(true);
        when(userRepository.userByUserName(anyString())).thenReturn(Optional.of(user));

        //when
        LoginResultDTO loginResultDTO = authenticationApplicationService.employeeLogin("lukas1", "password");

        //then
        verify(sessionRepository, times(1)).add(any());
    }

    @Test
    void basic_customer_login() throws AuthenticationFailed {
        reset(sessionRepository);
        //given
        User user = User.create(new UserId(UUID.randomUUID()), "userA", "max", "mustermann", List.of(UserRole.EMPLOYEE, UserRole.OPERATOR),List.of());
        when(ldapClient.customerCredentialsValid(any(), any())).thenReturn(true);
        when(userRepository.userByUserName(anyString())).thenReturn(Optional.of(user));

        //when
        LoginResultDTO loginResultDTO = authenticationApplicationService.customerLogin("lukas1", "password");

        //then
        verify(sessionRepository, times(1)).add(any());
    }

    @Test
    void roles_of_valid_session() throws SessionExpired {
        //given
        User user = User.create(
                new UserId(UUID.randomUUID()),
                "buttsoup-barnes",
                "Buut",
                "Soup",
                List.of(UserRole.EMPLOYEE),
                List.of()
        );
        Session session = Session.newForUser(user.getUserId());
        when(sessionRepository.sessionById(session.getSessionId())).thenReturn(Optional.of(session));
        when(userRepository.userById(user.getUserId())).thenReturn(Optional.of(user));

        //when
        boolean resultOperator = authenticationApplicationService.hasRole(session.getSessionId(), UserRole.OPERATOR);
        boolean resultEmployee = authenticationApplicationService.hasRole(session.getSessionId(), UserRole.EMPLOYEE);

        //then
        assertTrue(resultEmployee);
        assertFalse(resultOperator);
    }

    @Test
    void roles_of_invalid_session() throws NoSuchFieldException, IllegalAccessException {
        Session session = Session.newForUser(new UserId(UUID.randomUUID()));
        //make expired
        Field validUntil = Session.class.getDeclaredField("validUntil");
        validUntil.setAccessible(true);
        validUntil.set(session, Instant.now().minus(Duration.ofDays(1)));

        when(sessionRepository.sessionById(session.getSessionId())).thenReturn(Optional.of(session));

        assertThrows(SessionExpired.class, () -> authenticationApplicationService.hasRole(session.getSessionId(), UserRole.EMPLOYEE));
        verify(sessionRepository, times(1)).removeExpiredSessions();
    }

    @Test
    void when_invalid_credentials_then_login_fails() {
        //given
        String username = "fakeuser";
        String password = "fakepassword";
        when(ldapClient.employeeCredentialsValid(username, password)).thenReturn(false);

        //when - then
        assertThrows(AuthenticationFailed.class, () -> authenticationApplicationService.employeeLogin(username, password));
    }

    @Test
    void when_invalid_credentials_of_customer_then_login_fails() {
        //given
        String username = "fakeuser";
        String password = "fakepassword";
        when(ldapClient.customerCredentialsValid(username, password)).thenReturn(false);

        //when - then
        assertThrows(AuthenticationFailed.class, () -> authenticationApplicationService.customerLogin(username, password));
    }

    @Test
    void check_validity() throws SessionExpired {
        //given
        Session session = Session.newForUser(new UserId(UUID.randomUUID()));
        Session sessionSpy = spy(session);
        when(sessionRepository.sessionById(session.getSessionId())).thenReturn(Optional.of(sessionSpy));

        //when
        this.authenticationApplicationService.checkValidity(session.getSessionId().getValue());

        //then
        verify(sessionSpy).refreshDuration();
    }
}
