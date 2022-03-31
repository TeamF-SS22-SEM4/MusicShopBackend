package at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.servant;

import at.fhv.ss22.ea.f.communication.dto.CustomerDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.CustomerApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.RMIServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class CustomerSearchService extends UnicastRemoteObject {

    private final CustomerApplicationService customerApplicationService;

    public CustomerSearchService(CustomerApplicationService customerApplicationService) throws RemoteException {
        super(RMIServer.getPort());
        this.customerApplicationService = customerApplicationService;
    }

    public CustomerDTO customerById(UUID customerId) throws RemoteException {
        return customerApplicationService.customerById(customerId).orElse(null);
    }

    public List<CustomerDTO> customerListByIds(List<UUID> uuidList) {
        return customerApplicationService.customerListByIds(uuidList);
    }
}
