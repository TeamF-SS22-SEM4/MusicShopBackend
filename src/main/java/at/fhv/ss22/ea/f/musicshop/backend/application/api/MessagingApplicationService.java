package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.communication.dto.MessageDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RequiresRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;

import java.util.List;

public interface MessagingApplicationService {

    @RequiresRole(UserRole.OPERATOR)
    boolean publish(String sessionId, MessageDTO message) throws SessionExpired, NoPermissionForOperation;

    @RequiresRole(UserRole.EMPLOYEE)
    List<String> getSubscribedTopics(String sessionId) throws SessionExpired, NoPermissionForOperation;

    @RequiresRole(UserRole.EMPLOYEE)
    boolean subscribeTo(String sessionId, String topicName) throws SessionExpired, NoPermissionForOperation;

    @RequiresRole(UserRole.EMPLOYEE)
    boolean unsubscribeFrom(String sessionId, String topicName) throws SessionExpired, NoPermissionForOperation;
}
