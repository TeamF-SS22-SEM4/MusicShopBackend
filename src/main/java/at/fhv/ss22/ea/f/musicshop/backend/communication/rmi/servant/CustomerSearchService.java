package at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.servant;

import at.fhv.ss22.ea.f.communication.api.CustomerService;
import at.fhv.ss22.ea.f.communication.dto.CustomerDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.RMIClient;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.RMIServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class CustomerSearchService extends UnicastRemoteObject implements CustomerService {
    public CustomerSearchService() throws RemoteException {
        super(RMIServer.getPort());
    }

    @Override
    public CustomerDTO customerById(String sessionId, UUID uuid) throws RemoteException, SessionExpired, NoPermissionForOperation {
        // Check session here?
        return RMIClient.getRmiClient().getCustomerInternalService().customerById(uuid);
    }

    @Override
    public List<CustomerDTO> customerListByIds(String sessionId, List<UUID> uuidList) throws RemoteException, SessionExpired, NoPermissionForOperation {
        return RMIClient.getRmiClient().getCustomerInternalService().customerListByIds(uuidList);
    }

    @Override
    public List<CustomerDTO> search(String sessionId, String query) throws RemoteException, SessionExpired, NoPermissionForOperation {
        return RMIClient.getRmiClient().getCustomerInternalService().search(query);
    }
}
