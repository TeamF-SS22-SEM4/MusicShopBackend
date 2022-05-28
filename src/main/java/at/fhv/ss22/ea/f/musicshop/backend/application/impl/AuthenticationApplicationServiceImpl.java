package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.LoginResultDTO;
import at.fhv.ss22.ea.f.communication.exception.AuthenticationFailed;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.Logged;
import at.fhv.ss22.ea.f.musicshop.backend.communication.authentication.LdapClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.User;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.UserRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Local(AuthenticationApplicationService.class)
@Stateless
@Logged
public class AuthenticationApplicationServiceImpl implements AuthenticationApplicationService {
    private static final Logger logger = LogManager.getLogger(AuthenticationApplicationServiceImpl.class);

    @EJB private LdapClient ldapClient;
    @EJB private SessionRepository sessionRepository;
    @EJB private UserRepository userRepository;

    public AuthenticationApplicationServiceImpl() {}

    public AuthenticationApplicationServiceImpl(LdapClient ldapClient, SessionRepository sessionRepository, UserRepository userRepository) {
        this.ldapClient = ldapClient;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public LoginResultDTO employeeLogin(String username, String password) throws AuthenticationFailed {
        if (!ldapClient.employeeCredentialsValid(username, password)) {
            logger.warn("Failed to authenticate {} because of invalid credentials", username); //do NOT log the password
            throw new AuthenticationFailed();
        }

        return createSession(username);
    }

    @Override
    public boolean checkValidity(String sessionId) {

        Optional<Session> optSession = this.sessionRepository.sessionById(new SessionId(sessionId));
        optSession.ifPresent(s -> {
            try {
                s.refreshDuration();
            } catch (SessionExpired e) {
                throw new RuntimeException(e);
            }
        });
        return optSession.isPresent();
    }

    @Override
    public LoginResultDTO customerLogin(String username, String password) throws AuthenticationFailed {
        if (!ldapClient.customerCredentialsValid(username, password)) {
            logger.warn("Failed to authenticate {} because of invalid credentials", username); //do NOT log the password
            throw new AuthenticationFailed();
        }

        return createSession(username);
    }

    private LoginResultDTO createSession(String username) throws AuthenticationFailed {
        Optional<User> opt = userRepository.userByUserName(username);
        User user = opt.orElseThrow(AuthenticationFailed::new);
        Session session = Session.newForUser(user.getUserId());
        EntityManagerUtil.beginTransaction();

        sessionRepository.add(session);
        EntityManagerUtil.commit();

        logger.info("successfully logged {} in", username);

        return LoginResultDTO.builder()
                .withId(session.getSessionId().getValue())
                .withEmployeeId(user.getUserId().getUUID().toString())
                .withUsername(user.getUsername())
                .withRoles(user.getRoles().stream().map(UserRole::getNiceName).collect(Collectors.toList()))
                .withTopicNames(new ArrayList<>(user.getSubscribedTopics()))
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
        User user = userRepository.userById(session.getUserId()).orElseThrow(IllegalStateException::new);
        return user.hasRole(userRole);
    }
}
