package at.fhv.ss22.ea.f.musicshop.backend.communication;

import at.fhv.ss22.ea.f.communication.api.RMIFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RMIServer {
    private static int PORT = 12345; //default value, is overriden if env. variable is set
    private static String PROTOCOL = "rmi://";
    private static String HOST = "localhost"; //TODO maybe get values from .env
    private static String REMOTE_OBJECT_NAME = "RMIFactory";


    static {
        String envPort = System.getenv("RMI_PORT");
        if (envPort != null) {
            PORT = Integer.parseInt(envPort);
        }
    }
    public static int getPort() {return PORT;}

    public static void startRMIServer() {
        try {
            RMIFactory rmiFactory = new RMIFactoryImpl(PORT);
            LocateRegistry.createRegistry(PORT);

            Naming.rebind(PROTOCOL + HOST + ":" + PORT + "/" + REMOTE_OBJECT_NAME, rmiFactory);

            System.out.println("RMIFactory bound in registry");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
