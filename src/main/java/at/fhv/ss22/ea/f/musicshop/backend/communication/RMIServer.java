package at.fhv.ss22.ea.f.musicshop.backend.communication;

import at.fhv.ss22.ea.f.communication.api.RMIFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Optional;

public class RMIServer {
    private static int PORT = Integer.parseInt(System.getenv("RMI_PORT"));
    private static String PROTOCOL = "rmi://";
    private static String HOST = "localhost";
    private static String STUB = "/RMIFactory";

    public static void startRMIServer() {
        try {
            System.setProperty("java.rmi.server.hostname", Optional.ofNullable(System.getenv("RMI_HOSTNAME")).orElse("localhost"));
            RMIFactory rmiFactory = new RMIFactoryImpl();
            LocateRegistry.createRegistry(PORT);

            Naming.rebind(PROTOCOL + HOST + ":" + PORT + STUB, rmiFactory);

            System.out.println("RMIFactory bound in registry on port " + PORT);
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
