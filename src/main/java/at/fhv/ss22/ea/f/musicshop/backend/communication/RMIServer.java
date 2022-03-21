package at.fhv.ss22.ea.f.musicshop.backend.communication;

import at.fhv.ss22.ea.f.communication.api.RMIFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    private static String PROTOCOL = "rmi://";
    private static String HOST = "localhost";
    private static String STUB = "/RMIFactory";

    public static void startRMIServer() {
        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

            RMIFactory rmiFactory = new RMIFactoryImpl();

            Naming.rebind(PROTOCOL + HOST + STUB, rmiFactory);
            System.out.println("RMIFactory bound in registry");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
