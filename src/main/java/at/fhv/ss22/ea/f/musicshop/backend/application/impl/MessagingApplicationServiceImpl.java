package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.MessageDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.MessagingApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.Logged;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RequiresRole;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.SessionKey;
import at.fhv.ss22.ea.f.musicshop.backend.communication.jms.JMSClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.User;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.UserRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Local(MessagingApplicationService.class)
@Stateless
@Logged
public class MessagingApplicationServiceImpl implements MessagingApplicationService {

    @EJB private JMSClient jmsClient;
    @EJB private UserRepository userRepository;
    @EJB private SessionRepository sessionRepository;

    public MessagingApplicationServiceImpl() {}

    public MessagingApplicationServiceImpl(JMSClient jmsClient, UserRepository userRepository, SessionRepository sessionRepository) {
        this.jmsClient = jmsClient;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    @RequiresRole(UserRole.OPERATOR)
    public boolean publish(@SessionKey String sessionId, MessageDTO message) throws SessionExpired, NoPermissionForOperation {
        // Add new line between title and content, so it can be split up
        try {
            jmsClient.publishMessage(message.getTopicName(), message.getTitle() + "\n" + message.getContent());
        } catch (JMSException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public List<String> getSubscribedTopics(@SessionKey String sessionId) throws SessionExpired {
        Session session = sessionRepository.sessionById(new SessionId(sessionId)).orElseThrow(SessionExpired::new);
        User user = userRepository.userById(session.getUserId()).orElseThrow(IllegalStateException::new);
        return new ArrayList<>(user.getSubscribedTopics());
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public void updateLastViewed(@SessionKey String sessionId, LocalDateTime lastViewedMessages) throws SessionExpired {
        Session session = sessionRepository.sessionById(new SessionId(sessionId)).orElseThrow(SessionExpired::new);
        User user = userRepository.userById(session.getUserId()).orElseThrow(IllegalStateException::new);

        EntityManagerUtil.beginTransaction();
        user.updateLastViewed(lastViewedMessages);
        EntityManagerUtil.commit();
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public LocalDateTime getLastViewed(@SessionKey String sessionId) throws SessionExpired {
        Session session = sessionRepository.sessionById(new SessionId(sessionId)).orElseThrow(SessionExpired::new);
        User user = userRepository.userById(session.getUserId()).orElseThrow(IllegalStateException::new);
        return user.getLastViewed();
    }
}
