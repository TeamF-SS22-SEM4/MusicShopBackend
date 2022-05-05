package at.fhv.ss22.ea.f.musicshop.backend.communication.rest;

import at.fhv.ss22.ea.f.communication.dto.LoginResultDTO;
import at.fhv.ss22.ea.f.communication.exception.AuthenticationFailed;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rest.objects.Credentials;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginController {

    @EJB
    private AuthenticationApplicationService authenticationApplicationService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "403", description = "invalid credentials")
    @APIResponseSchema(value = LoginResultDTO.class, responseCode = "200")
    public Response login(@RequestBody Credentials credentials) {
        try {
            LoginResultDTO loginResult = authenticationApplicationService.customerLogin(credentials.username, credentials.password);
            return Response.ok().entity(loginResult).build();
        } catch (Exception e) {
            return ExceptionHandler.handleException(e);
        }
    }
}
