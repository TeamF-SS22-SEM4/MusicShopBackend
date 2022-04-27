package at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.servant;

import at.fhv.ss22.ea.f.communication.api.MessagingService;
import at.fhv.ss22.ea.f.communication.dto.MessageDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.MessagingApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.RMIServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.List;

public class MessagingServiceServant extends UnicastRemoteObject implements MessagingService {
    private MessagingApplicationService messagingApplicationService;

    public MessagingServiceServant(MessagingApplicationService messagingApplicationService) throws RemoteException {
        super(RMIServer.getPort());
        this.messagingApplicationService = messagingApplicationService;
    }

    @Override
    public boolean publish(String sessionId, MessageDTO message) throws RemoteException, SessionExpired, NoPermissionForOperation {
        return messagingApplicationService.publish(sessionId, message);
    }

    @Override
    public List<String> getSubscribedTopics(String sessionId) throws RemoteException, SessionExpired, NoPermissionForOperation {
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
