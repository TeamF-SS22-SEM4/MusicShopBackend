package at.fhv.ss22.ea.f.musicshop.backend.communication.rest;


import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductApplicationService;

import javax.ejb.EJB;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("test")
public class TestController {

    @EJB
    private ProductApplicationService productApplicationService;

    @GET
    @Path("hello")
    public String test() {
        return "hello world !" + productApplicationService;
    }
}
