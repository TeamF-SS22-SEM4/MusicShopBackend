package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.communication.dto.MessageDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;

import javax.jms.JMSException;
import java.util.List;

public interface MessagingApplicationService {

    //TODO role annotations
    boolean publish(String sessionId, MessageDTO message) throws SessionExpired, NoPermissionForOperation, JMSException;

    List<String> getSubscribedTopics(String sessionId) throws SessionExpired, NoPermissionForOperation;

    boolean subscribeTo(String sessionId, String topicName) throws SessionExpired, NoPermissionForOperation;

    boolean unsubscribeFrom(String sessionId, String topicName) throws SessionExpired, NoPermissionForOperation;
}
