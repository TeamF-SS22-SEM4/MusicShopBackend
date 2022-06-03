package at.fhv.ss22.ea.f.musicshop.backend.communication.ejb;

import at.fhv.ss22.ea.f.communication.api.OrderingService;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierOrderDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.OrderingApplicationService;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

@Remote(OrderingService.class)
@Stateless
public class OrderingServiceImpl implements OrderingService {
    @EJB
    private OrderingApplicationService orderingApplicationService;

    @Override
    public boolean placeOrder(String sessionId, SoundCarrierOrderDTO orderDTO) throws SessionExpired, NoPermissionForOperation {
        return this.orderingApplicationService.placeOrder(sessionId, orderDTO);
    }

    @Override
    public boolean approveOrder(String sessionId, SoundCarrierOrderDTO orderDTO) throws SessionExpired, NoPermissionForOperation {
        return this.orderingApplicationService.approveOrder(sessionId, orderDTO);
    }
}
