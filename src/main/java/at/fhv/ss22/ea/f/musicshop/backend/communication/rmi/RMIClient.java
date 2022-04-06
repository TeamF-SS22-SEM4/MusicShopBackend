package at.fhv.ss22.ea.f.musicshop.backend.communication.rmi;

import at.fhv.ss22.ea.f.communication.api.CustomerService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClient {
    private static RMIClient rmiClient;
    private CustomerService customerService;
    private static int PORT = 1099; // TODO: Add to env file
    private static String PROTOCOL = "rmi://";
    private static String HOST = "10.0.40.171"; // TODO: add to env file
    private static String STUB = "/CustomerService";

    private RMIClient() {
        try {
            customerService = (CustomerService) Naming.lookup(PROTOCOL + HOST + ":" + PORT + STUB);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public static RMIClient getRmiClient() {
        if(rmiClient == null) {
            rmiClient = new RMIClient();
        }

        return rmiClient;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }
}
