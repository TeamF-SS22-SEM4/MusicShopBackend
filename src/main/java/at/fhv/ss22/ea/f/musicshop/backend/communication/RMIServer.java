package at.fhv.ss22.ea.f.musicshop.backend.communication;

import at.fhv.ss22.ea.f.communication.api.RMIFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RMIServer {
    private static int PORT = 12345; //default value, is overriden if env. variable is set
    private static String PROTOCOL = "rmi://";
    private static String HOST = "localhost"; //default value, is overriden if env. variable is set
    private static String REMOTE_OBJECT_NAME = "RMIFactory";

    static {
        String envPort = System.getenv("RMI_PORT");
        if (envPort != null) {
            PORT = Integer.parseInt(envPort);
        }

        String envHostName = System.getenv("RMI_HOSTNAME");
        if(envHostName != null) {
            HOST = envHostName;
        }
    }
    public static int getPort() {return PORT;}

    public static void startRMIServer() {
        try {
            System.setProperty("java.rmi.server.hostname", HOST);

            RMIFactory rmiFactory = new RMIFactoryImpl(PORT);

            LocateRegistry.createRegistry(PORT);

            Naming.rebind(PROTOCOL + HOST + ":" + PORT + "/" + REMOTE_OBJECT_NAME, rmiFactory);

            System.out.println("RMIFactory bound in registry on port " + PORT);
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
