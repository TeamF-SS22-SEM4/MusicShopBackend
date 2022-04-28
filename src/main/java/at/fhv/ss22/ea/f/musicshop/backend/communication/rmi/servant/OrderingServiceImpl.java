package at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.servant;

import at.fhv.ss22.ea.f.communication.api.OrderingService;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierOrderDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.OrderingApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.RMIServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class OrderingServiceImpl extends UnicastRemoteObject implements OrderingService {
    private OrderingApplicationService orderingApplicationService;

    public OrderingServiceImpl(OrderingApplicationService orderingApplicationService) throws RemoteException {
        super(RMIServer.getPort());
        this.orderingApplicationService = orderingApplicationService;
    }

    @Override
    public boolean placeOrder(String sessionId, SoundCarrierOrderDTO orderDTO) throws RemoteException, SessionExpired, NoPermissionForOperation {
        return this.orderingApplicationService.placeOrder(sessionId, orderDTO);
    }

    @Override
    public boolean approveOrder(String sessionId, SoundCarrierOrderDTO orderDTO) throws RemoteException, SessionExpired, NoPermissionForOperation {
        return this.orderingApplicationService.approveOrder(sessionId, orderDTO);
    }

    @Override
    public boolean denyOrder(String sessionId, SoundCarrierOrderDTO orderDTO) throws RemoteException, SessionExpired, NoPermissionForOperation {
        return this.orderingApplicationService.denyOrder(sessionId, orderDTO);
    }
}
