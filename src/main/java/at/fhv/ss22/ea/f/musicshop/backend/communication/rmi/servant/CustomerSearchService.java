package at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.servant;

import at.fhv.ss22.ea.f.communication.api.CustomerService;
import at.fhv.ss22.ea.f.communication.dto.CustomerDTO;
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
    public CustomerDTO customerById(UUID uuid) throws RemoteException {
        return RMIClient.getRmiClient().getCustomerService().customerById(uuid);
    }

    @Override
    public List<CustomerDTO> customerListByIds(List<UUID> uuidList) throws RemoteException {
        return RMIClient.getRmiClient().getCustomerService().customerListByIds(uuidList);
    }

    @Override
    public List<CustomerDTO> search(String query) throws RemoteException {
        return RMIClient.getRmiClient().getCustomerService().search(query);
    }
}
