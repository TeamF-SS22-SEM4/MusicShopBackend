package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.LoginResultDTO;
import at.fhv.ss22.ea.f.communication.exception.AuthenticationFailed;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.authentication.LdapClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.Employee;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EmployeeRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Local(AuthenticationApplicationService.class)
@Stateless
public class AuthenticationApplicationServiceImpl implements AuthenticationApplicationService {
    private static final Logger logger = LogManager.getLogger(AuthenticationApplicationServiceImpl.class);

    private LdapClient ldapClient; // Needs to be a local stateless bean
    private SessionRepository sessionRepository; // Needs to be a local stateless bean
    private EmployeeRepository employeeRepository; // Needs to be a local stateless bean

    public AuthenticationApplicationServiceImpl(LdapClient ldapClient, SessionRepository sessionRepository, EmployeeRepository employeeRepository) {
        this.ldapClient = ldapClient;
        this.sessionRepository = sessionRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public LoginResultDTO login(String username, String password) throws AuthenticationFailed {
        if (!ldapClient.credentialsValid(username, password)) {
            logger.warn("Failed to authenticate {} because of invalid credentials", username); //do NOT log the password
            throw new AuthenticationFailed();
        }
        Optional<Employee> opt = employeeRepository.employeeByUserName(username);
        Employee employee = opt.orElseThrow(AuthenticationFailed::new);
        Session session = Session.newForEmployee(employee.getEmployeeId());
        EntityManagerUtil.beginTransaction();

        sessionRepository.add(session);
        EntityManagerUtil.commit();

        logger.info("successfuly logged {} in", username);

        // with .withTopicNames(employee.getSubscribedTopics()) --> Employee not Serializeable?
        return LoginResultDTO.builder()
                .withId(session.getSessionId().getValue())
                .withEmployeeId(employee.getEmployeeId().getUUID().toString())
                .withRoles(employee.getRoles().stream().map(UserRole::getNiceName).collect(Collectors.toList()))
                .withTopicNames(new ArrayList<>(employee.getSubscribedTopics()))
                .build();
    }

    @Override
    public boolean hasRole(SessionId sessionId, UserRole userRole) throws SessionExpired {
        Session session = sessionRepository.sessionById(sessionId).orElseThrow(SessionExpired::new);
        if (session.isExpired()) {
            sessionRepository.removeExpiredSessions();
            throw new SessionExpired();
        }
        EntityManagerUtil.beginTransaction();
        session.refreshDuration();
        EntityManagerUtil.commit();
        Employee employee = employeeRepository.employeeById(session.getEmployeeId()).orElseThrow(IllegalStateException::new);
        return employee.hasRole(userRole);
    }
}
