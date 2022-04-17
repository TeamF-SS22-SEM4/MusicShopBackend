package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.MessageDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.MessagingApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.jms.JMSClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EmployeeRepository;

import javax.jms.JMSException;
import java.util.List;

public class MessagingApplicationServiceImpl implements MessagingApplicationService {

    private JMSClient jmsClient;

    private EmployeeRepository employeeRepository; //needed to get subscribed list from employee

    public MessagingApplicationServiceImpl(JMSClient jmsClient, EmployeeRepository employeeRepository) {
        this.jmsClient = jmsClient;
        this.employeeRepository = employeeRepository;
    }

    //TODO implementations and RoleAnnotation
    @Override
    public boolean publish(String sessionId, MessageDTO message) throws SessionExpired, NoPermissionForOperation, JMSException {
        // Add new line between title and content, so it can be split up
        jmsClient.publishMessage(message.getTopicName(), message.getTitle() + "\n" + message.getContent());
        return true;
    }

    @Override
    public List<String> getSubscribedTopics(String sessionId) throws SessionExpired, NoPermissionForOperation {
        return null;
    }

    @Override
    public boolean subscribeTo(String sessionId, String topicName) throws SessionExpired, NoPermissionForOperation {
        return false;
    }

    @Override
    public boolean unsubscribeFrom(String sessionId, String topicName) throws SessionExpired, NoPermissionForOperation {
        return false;
    }
}
