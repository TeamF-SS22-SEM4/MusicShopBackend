package at.fhv.ss22.ea.f.musicshop.backend.communication.ejb;

import at.fhv.ss22.ea.f.communication.api.CustomerService;
import at.fhv.ss22.ea.f.communication.dto.CustomerDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.CustomerApplicationService;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

@Remote(CustomerService.class)
@Stateless
public class CustomerSearchService implements CustomerService {

    @EJB
    private CustomerApplicationService customerApplicationService;

    // TODO: Maybe only ejb that has to throw RemoteException (Adding to interface in sharedlib)
    @Override
    public CustomerDTO customerById(String sessionId, UUID uuid) throws SessionExpired, NoPermissionForOperation {
        try {
            return customerApplicationService.customerById(sessionId, uuid);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CustomerDTO> customerListByIds(String sessionId, List<UUID> uuidList) throws SessionExpired, NoPermissionForOperation {
        try {
            return customerApplicationService.customerListByIds(sessionId, uuidList);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CustomerDTO> search(String sessionId, String query) throws SessionExpired, NoPermissionForOperation {
        try {
            return customerApplicationService.search(sessionId, query);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
