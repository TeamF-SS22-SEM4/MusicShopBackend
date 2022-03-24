package at.fhv.ss22.ea.f.musicshop.backend.communication;

import at.fhv.ss22.ea.f.communication.api.RMIFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RMIServer {
    private static int PORT = 12345;
    private static String PROTOCOL = "rmi://";
    private static String HOST = "10.0.40.170";
    private static String STUB = "/RMIFactory";

    public static void startRMIServer() {
        try {
            RMIFactory rmiFactory = new RMIFactoryImpl();
            LocateRegistry.createRegistry(PORT).rebind("RMIFactory", rmiFactory);

            System.out.println("RMIFactory bound in registry");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
