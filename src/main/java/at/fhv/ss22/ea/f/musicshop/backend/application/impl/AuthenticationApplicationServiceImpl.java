package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.LoginResultDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.exceptions.AuthenticationFailed;
import at.fhv.ss22.ea.f.musicshop.backend.communication.authentication.LdapClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.Employee;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EmployeeRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;

import java.util.Optional;
import java.util.stream.Collectors;

public class AuthenticationApplicationServiceImpl implements AuthenticationApplicationService {

    private LdapClient ldapClient;
    private SessionRepository sessionRepository;
    private EmployeeRepository employeeRepository;

    public AuthenticationApplicationServiceImpl(LdapClient ldapClient, SessionRepository sessionRepository, EmployeeRepository employeeRepository) {
        this.ldapClient = ldapClient;
        this.sessionRepository = sessionRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public LoginResultDTO login(String username, String password) throws AuthenticationFailed {
        if (!ldapClient.credentialsValid(username, password)) {
            throw new AuthenticationFailed();
        }
        Optional<Employee> opt = employeeRepository.employeeByUserName(username);
        Employee employee = opt.orElseThrow(AuthenticationFailed::new);
        Session session = Session.newForEmployee(employee.getEmployeeId());
        EntityManagerUtil.beginTransaction();

        sessionRepository.add(session);

        EntityManagerUtil.commit();

        return LoginResultDTO.builder()
                .withId(session.getSessionId().getValue())
                .withRoles(employee.getRoles().stream().map(UserRole::getNiceName).collect(Collectors.toList()))
                .build();
    }

    //TODO method for checking if sessionId is valid

}