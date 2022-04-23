package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;

public interface OrderingApplicationService {

    boolean placeOrder(String sessionId, SoundCarrierOrderDTO orderDTO) throws SessionExpired, NoPermissionForOperation;

    boolean approveOrder(String sessionId, SoundCarrierOrderDTO orderDTO) throws SessionExpired, NoPermissionForOperation;

    boolean denyOrder(String sessionId, SoundCarrierOrderDTO orderDTO) throws SessionExpired, NoPermissionForOperation;

}
