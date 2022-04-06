package at.fhv.ss22.ea.f.musicshop.backend;

import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.RMIServer;

public class Application {
    public static void main(String[] args) {
        System.out.println("Starting Server....");
        RMIServer.startRMIServer();

    }
}
