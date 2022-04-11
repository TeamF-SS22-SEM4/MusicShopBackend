package at.fhv.ss22.ea.f.musicshop.backend.communication.internal;

import at.fhv.ss22.ea.f.communication.internal.CustomerInternalService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class CustomerRMIClient {
    private static CustomerRMIClient customerRmiClient;
    private CustomerInternalService customerService;
    private static int PORT = 1099; // TODO: Add to env file
    private static String PROTOCOL = "rmi://";
    private static String HOST = "10.0.40.171"; // TODO: add to env file
    private static String STUB = "/CustomerService";

    private CustomerRMIClient() {
        try {
            customerService = (CustomerInternalService) Naming.lookup(PROTOCOL + HOST + ":" + PORT + STUB);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public static CustomerRMIClient getCustomerRmiClient() {
        if(customerRmiClient == null) {
            customerRmiClient = new CustomerRMIClient();
        }

        return customerRmiClient;
    }

    public CustomerInternalService getCustomerInternalService() {
        return customerService;
    }
}
