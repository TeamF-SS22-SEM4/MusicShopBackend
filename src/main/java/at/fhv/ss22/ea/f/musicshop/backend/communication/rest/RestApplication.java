package at.fhv.ss22.ea.f.musicshop.backend.communication.rest;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api/v1")
public class RestApplication extends javax.ws.rs.core.Application {
    public static final String SESSION_HEADER = "session-id";
}
