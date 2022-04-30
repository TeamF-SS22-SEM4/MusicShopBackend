package at.fhv.ss22.ea.f.musicshop.backend.communication.internal;

import at.fhv.ss22.ea.f.communication.internal.CustomerInternalService;

import javax.ejb.Local;
import javax.ejb.Stateless;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

@Stateless
@Local(CustomerRMIClient.class)
public class CustomerRMIClientImpl implements CustomerRMIClient {
    private CustomerInternalService customerService;
    private static String PORT =  System.getenv("CUSTOMER_SERVICE_RMI_PORT");
    private static String PROTOCOL = "rmi://";
    private static String HOST = System.getenv("CUSTOMER_SERVICE_RMI_HOSTNAME");
    private static String OBJECT_NAME = "CustomerInternalService";

    public CustomerRMIClientImpl() {
        this.reconnect();
    }

    @Override
    public void reconnect() {
        try {
            customerService = (CustomerInternalService) Naming.lookup(PROTOCOL + HOST + ":" + PORT + "/" + OBJECT_NAME);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CustomerInternalService getCustomerInternalService() {
        return customerService;
    }
}
