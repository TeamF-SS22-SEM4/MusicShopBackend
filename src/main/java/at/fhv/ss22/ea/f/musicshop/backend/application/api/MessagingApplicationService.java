package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.communication.dto.MessageDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;

import java.time.LocalDateTime;
import java.util.List;

public interface MessagingApplicationService {

    boolean publish(String sessionId, MessageDTO message) throws SessionExpired, NoPermissionForOperation;

    List<String> getSubscribedTopics(String sessionId) throws SessionExpired, NoPermissionForOperation;

    void updateLastViewed(String sessionId, LocalDateTime lastViewedMessages) throws SessionExpired, NoPermissionForOperation;

    LocalDateTime getLastViewed(String sessionId) throws SessionExpired, NoPermissionForOperation;
}
