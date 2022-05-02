package at.fhv.ss22.ea.f.musicshop.backend.communication.rest;

import at.fhv.ss22.ea.f.musicshop.backend.communication.rest.objects.Credentials;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("credentials")
public class LoginController {

    @POST
    public Response login(Credentials credentials) {
        return Response.serverError().build();
    }

}
