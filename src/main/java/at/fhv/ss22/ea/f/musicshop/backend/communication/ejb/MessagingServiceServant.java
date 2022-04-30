package at.fhv.ss22.ea.f.musicshop.backend.communication.ejb;

import at.fhv.ss22.ea.f.communication.api.MessagingService;
import at.fhv.ss22.ea.f.communication.dto.MessageDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.MessagingApplicationService;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.util.List;

@Remote(MessagingService.class)
@Stateless
public class MessagingServiceServant implements MessagingService {
    @EJB
    private MessagingApplicationService messagingApplicationService;

    @Override
    public boolean publish(String sessionId, MessageDTO message) throws SessionExpired, NoPermissionForOperation {
        return messagingApplicationService.publish(sessionId, message);
    }

    @Override
    public List<String> getSubscribedTopics(String sessionId) throws SessionExpired, NoPermissionForOperation {
        return messagingApplicationService.getSubscribedTopics(sessionId);
    }

    @Override
    public void updateLastViewed(String sessionId, LocalDateTime lastViewedMessages) throws SessionExpired, NoPermissionForOperation {
        messagingApplicationService.updateLastViewed(sessionId, lastViewedMessages);
    }

    @Override
    public LocalDateTime getLastViewed(String sessionId) throws SessionExpired, NoPermissionForOperation {
        return messagingApplicationService.getLastViewed(sessionId);
    }
}
