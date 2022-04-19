package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.communication.dto.CustomerDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RequiresRole;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.SessionKey;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface CustomerApplicationService {

    @RequiresRole(UserRole.EMPLOYEE)
    CustomerDTO customerById(@SessionKey String sessionId, UUID uuid) throws RemoteException, SessionExpired, NoPermissionForOperation;

    @RequiresRole(UserRole.EMPLOYEE)
    List<CustomerDTO> customerListByIds(@SessionKey String sessionId, List<UUID> uuidList) throws RemoteException, SessionExpired, NoPermissionForOperation;

    @RequiresRole(UserRole.EMPLOYEE)
    List<CustomerDTO> search(@SessionKey String sessionId, String query) throws RemoteException, SessionExpired, NoPermissionForOperation;
}
