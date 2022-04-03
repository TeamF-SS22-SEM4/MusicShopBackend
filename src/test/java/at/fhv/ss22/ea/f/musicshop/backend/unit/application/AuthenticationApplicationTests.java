package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.LoginResultDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.exceptions.AuthenticationFailed;
import at.fhv.ss22.ea.f.musicshop.backend.communication.authentication.LdapClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.Employee;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EmployeeRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthenticationApplicationTests {

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
}
