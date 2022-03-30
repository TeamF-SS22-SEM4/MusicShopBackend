package at.fhv.ss22.ea.f.musicshop.backend.communication;

import at.fhv.ss22.ea.f.communication.api.RMIFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RMIServer {
    private static int PORT = Integer.parseInt(System.getenv("RMI_PORT"));
    private static String PROTOCOL = "rmi://";
    private static String RMI_REGISTRY_HOST = "localhost"; // NOTE: this value doesn't have the same
    // purpose as the "RMI_HOST" environment variable
    private static String REMOTE_OBJECT_NAME = "RMIFactory";

    public static int getPort() {return PORT;}

    public static void startRMIServer() {
        try {
            //needed because else stub-endpoints are bound to the standard docker ip-network (172.- something)
            // which makes our remote objects then unreachable
            System.setProperty("java.rmi.server.hostname", System.getenv("RMI_HOSTNAME"));

            RMIFactory rmiFactory = new RMIFactoryImpl(PORT);
            LocateRegistry.createRegistry(PORT);
            Naming.rebind(PROTOCOL + RMI_REGISTRY_HOST + ":" + PORT + "/" + REMOTE_OBJECT_NAME, rmiFactory);

            System.out.println("RMIFactory bound in registry on port " + PORT);
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
