package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.CustomerDTO;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.CustomerApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RequiresRole;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.SessionKey;
import at.fhv.ss22.ea.f.musicshop.backend.communication.internal.CustomerRMIClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

@Local(CustomerApplicationService.class)
@Stateless
public class CustomerApplicationServiceImpl implements CustomerApplicationService {

    @EJB private CustomerRMIClient client;

    public CustomerApplicationServiceImpl() {}

    public CustomerApplicationServiceImpl(CustomerRMIClient rmiClient) {
        this.client = rmiClient;
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public CustomerDTO customerById(@SessionKey String sessionId, UUID uuid) throws RemoteException {
        return client.getCustomerInternalService().customerById(uuid);
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public List<CustomerDTO> customerListByIds(@SessionKey String sessionId, List<UUID> uuidList) throws RemoteException {
        return client.getCustomerInternalService().customerListByIds(uuidList);

    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public List<CustomerDTO> search(@SessionKey String sessionId, String query) throws RemoteException, SessionExpired {
        return client.getCustomerInternalService().search(query);
    }
}
