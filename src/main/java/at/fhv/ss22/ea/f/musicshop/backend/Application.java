package at.fhv.ss22.ea.f.musicshop.backend;

import at.fhv.ss22.ea.f.musicshop.backend.communication.RMIServer;

public class Application {
    public static void main(String[] args) {
        RMIServer.startRMIServer();
    }
}
