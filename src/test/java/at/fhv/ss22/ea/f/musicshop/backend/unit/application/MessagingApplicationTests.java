package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.communication.internal.CustomerInternalService;
import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.MessagingApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.Employee;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EmployeeRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MessagingApplicationTests {
    private MessagingApplicationService messagingApplicationService = InstanceProvider.getTestingMessagingApplicationService();

    private AuthenticationApplicationService authenticationService = InstanceProvider.getMockedAuthenticationApplicationService();

    private SessionRepository sessionRepository = InstanceProvider.getMockedSessionRepository();

    private EmployeeRepository employeeRepository = InstanceProvider.getMockedEmployeeRepository();

    @BeforeAll
    void setup() throws SessionExpired {
        when(authenticationService.hasRole(any(), any())).thenReturn(true);
    }

    @Test
    void update_last_viewed() throws SessionExpired, NoPermissionForOperation {
        // given
        LocalDateTime lastViewedExpected = LocalDateTime.now();
        UUID employeeIdUUID = UUID.randomUUID();
        EmployeeId employeeIdExpected = new EmployeeId(employeeIdUUID);
        String usernameExpected = "john42";
        String firstnameExpected = "John";
        String lastNameExpected = "Doe";

        Employee employee = Employee.create(
                employeeIdExpected,
                usernameExpected,
                firstnameExpected,
                lastNameExpected,
                List.of(UserRole.EMPLOYEE),
                Collections.emptyList()
        );

        when(sessionRepository.sessionById(any())).thenReturn(Optional.of(Session.newForEmployee(employeeIdExpected)));
        when(employeeRepository.employeeById(employeeIdExpected)).thenReturn(Optional.of(employee));

        // when
        messagingApplicationService.updateLastViewed("some", lastViewedExpected);
        LocalDateTime lastViewedActual = employee.getLastViewed();

        // then
        assertEquals(lastViewedExpected, lastViewedActual);
    }
}
