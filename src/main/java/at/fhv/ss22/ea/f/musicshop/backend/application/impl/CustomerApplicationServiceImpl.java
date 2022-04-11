package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.CustomerDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.CustomerApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.internal.CustomerRMIClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public class CustomerApplicationServiceImpl implements CustomerApplicationService {

    private CustomerRMIClient client;

    private AuthenticationApplicationService authenticationService;

    public CustomerApplicationServiceImpl(AuthenticationApplicationService authenticationService, CustomerRMIClient rmiClient) {
        this.client = rmiClient;
        this.authenticationService = authenticationService;
    }

    @Override
    public CustomerDTO customerById(String sessionId, UUID uuid) throws RemoteException, SessionExpired, NoPermissionForOperation {
        if (!authenticationService.hasRole(new SessionId(sessionId), UserRole.EMPLOYEE)) {
            throw new NoPermissionForOperation();
        }
        return client.getCustomerInternalService().customerById(uuid);
    }

    @Override
    public List<CustomerDTO> customerListByIds(String sessionId, List<UUID> uuidList) throws RemoteException, SessionExpired, NoPermissionForOperation {
        if (!authenticationService.hasRole(new SessionId(sessionId), UserRole.EMPLOYEE)) {
            throw new NoPermissionForOperation();
        }
        return client.getCustomerInternalService().customerListByIds(uuidList);
    }

    @Override
    public List<CustomerDTO> search(String sessionId, String query) throws RemoteException, SessionExpired, NoPermissionForOperation {
        if (!authenticationService.hasRole(new SessionId(sessionId), UserRole.EMPLOYEE)) {
            throw new NoPermissionForOperation();
        }
        return client.getCustomerInternalService().search(query);
    }
}
