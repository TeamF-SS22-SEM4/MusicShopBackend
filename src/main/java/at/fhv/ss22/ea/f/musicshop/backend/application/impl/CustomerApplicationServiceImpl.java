package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.CustomerDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.CustomerApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RequiresRole;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.SessionKey;
import at.fhv.ss22.ea.f.musicshop.backend.communication.internal.CustomerRMIClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public class CustomerApplicationServiceImpl implements CustomerApplicationService {

    private CustomerRMIClient client;

    public CustomerApplicationServiceImpl(CustomerRMIClient rmiClient) {
        this.client = rmiClient;
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public CustomerDTO customerById(@SessionKey String sessionId, UUID uuid) throws RemoteException, SessionExpired, NoPermissionForOperation {
        CustomerDTO response;
        try {
            response = client.getCustomerInternalService().customerById(uuid);
        } catch (NoSuchObjectException e) {
            //this error can occur when the customer-db-service is restarted and thus invalidates the remote-object reference
            // retry once with a fresh remote-reference
            client.reconnect();
            response = client.getCustomerInternalService().customerById(uuid);
        }
        return response;
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public List<CustomerDTO> customerListByIds(@SessionKey String sessionId, List<UUID> uuidList) throws RemoteException, SessionExpired, NoPermissionForOperation {
        List<CustomerDTO> response;
        try {
            response = client.getCustomerInternalService().customerListByIds(uuidList);
        } catch (NoSuchObjectException e) {
            //this error can occur when the customer-db-service is restarted and thus invalidates the remote-object reference
            // retry once with a fresh remote-reference
            client.reconnect();
            response = client.getCustomerInternalService().customerListByIds(uuidList);
        }
        return response;
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public List<CustomerDTO> search(@SessionKey String sessionId, String query) throws RemoteException, SessionExpired, NoPermissionForOperation {
        List<CustomerDTO> response;
        try {
            response = client.getCustomerInternalService().search(query);
        } catch (NoSuchObjectException e) {
            //this error can occur when the customer-db-service is restarted and thus invalidates the remote-object reference
            // retry once with a fresh remote-reference
            client.reconnect();
            response = client.getCustomerInternalService().search(query);
        }
        return response;
    }
    //TODO refactor the NoSuchObject catch, could probably move it to an invocation handler, because the code is very similar in all methods
}
