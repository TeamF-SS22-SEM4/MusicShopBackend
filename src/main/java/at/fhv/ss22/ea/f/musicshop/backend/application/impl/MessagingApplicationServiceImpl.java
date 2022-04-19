package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.MessageDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.MessagingApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RequiresRole;
import at.fhv.ss22.ea.f.musicshop.backend.communication.jms.JMSClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.Employee;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EmployeeRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;

import javax.jms.JMSException;
import java.util.List;

public class MessagingApplicationServiceImpl implements MessagingApplicationService {

    private JMSClient jmsClient;

    private EmployeeRepository employeeRepository; //needed to get subscribed list from employee

    private SessionRepository sessionRepository;

    public MessagingApplicationServiceImpl(JMSClient jmsClient, EmployeeRepository employeeRepository, SessionRepository sessionRepository) {
        this.jmsClient = jmsClient;
        this.employeeRepository = employeeRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    @RequiresRole(UserRole.OPERATOR)
    public boolean publish(String sessionId, MessageDTO message) throws SessionExpired, NoPermissionForOperation {
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
    public List<String> getSubscribedTopics(String sessionId) throws SessionExpired, NoPermissionForOperation {
        Session session = sessionRepository.sessionById(new SessionId(sessionId)).orElseThrow(SessionExpired::new);
        Employee employee = employeeRepository.employeeById(session.getEmployeeId()).orElseThrow(IllegalStateException::new);
        return employee.getSubscribedTopics();
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public boolean subscribeTo(String sessionId, String topicName) throws SessionExpired, NoPermissionForOperation {
        Session session = sessionRepository.sessionById(new SessionId(sessionId)).orElseThrow(SessionExpired::new);
        Employee employee = employeeRepository.employeeById(session.getEmployeeId()).orElseThrow(IllegalStateException::new);
        employee.subscribeTo(topicName);
        return true;
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public boolean unsubscribeFrom(String sessionId, String topicName) throws SessionExpired, NoPermissionForOperation {
        Session session = sessionRepository.sessionById(new SessionId(sessionId)).orElseThrow(SessionExpired::new);
        Employee employee = employeeRepository.employeeById(session.getEmployeeId()).orElseThrow(IllegalStateException::new);
        return employee.unsubscribeFrom(topicName);
    }
}
