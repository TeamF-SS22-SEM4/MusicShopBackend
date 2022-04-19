package at.fhv.ss22.ea.f.musicshop.backend.communication.internal;

import at.fhv.ss22.ea.f.communication.internal.CustomerInternalService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class CustomerRMIClient {
    private CustomerInternalService customerService;
    private static String PORT =  System.getenv("CUSTOMER_SERVICE_RMI_PORT");
    private static String PROTOCOL = "rmi://";
    private static String HOST = System.getenv("CUSTOMER_SERVICE_RMI_HOSTNAME");
    private static String OBJECT_NAME = "CustomerInternalService";

    public CustomerRMIClient() {
        this.reconnect();
    }

    public void reconnect() {
        try {
            customerService = (CustomerInternalService) Naming.lookup(PROTOCOL + HOST + ":" + PORT + "/" + OBJECT_NAME);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public CustomerInternalService getCustomerInternalService() {
        return customerService;
    }
}
