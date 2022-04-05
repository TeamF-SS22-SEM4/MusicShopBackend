package at.fhv.ss22.ea.f.musicshop.backend;

import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.RMIClient;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.RMIServer;

import java.rmi.RemoteException;

public class Application {
    public static void main(String[] args) {
        System.out.println("Starting Server....");
        RMIServer.startRMIServer();

        try {
            System.out.println("Try to get RMI Stub from CustomerDataService");
            RMIClient.getRmiClient().getCustomerService().search("A").forEach(System.out::println);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        System.out.println("Retrieved RMI Stub");
    }
}
