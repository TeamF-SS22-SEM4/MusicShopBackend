package at.fhv.ss22.ea.f.musicshop.backend.communication.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public class ProductController {

    @GET
    @Path("product/{id}")
    public Response productById(@HeaderParam("session-id") String sessionId, @PathParam("id") String productId) {

        return Response.serverError().build();
    }

    @GET
    @Path("products")
    public Response search(@HeaderParam("session-id") String sessionId, @QueryParam("search") @DefaultValue("") String query) {
        return Response.serverError().build();
    }
}
