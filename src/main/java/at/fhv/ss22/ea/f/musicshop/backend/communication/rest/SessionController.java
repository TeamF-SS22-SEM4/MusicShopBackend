package at.fhv.ss22.ea.f.musicshop.backend.communication.rest;

import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.ejb.EJB;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("validateSession")
public class SessionController {

    @EJB
    private AuthenticationApplicationService authenticationApplicationService;


    //not really resource oriented api, but couldn't come up with a "validate" resource
    @POST
    @APIResponse(responseCode = "200", description = "valid session")
    @APIResponse(responseCode = "404", description = "invalid session")
    @Operation(operationId = "validateSession")
    public Response validateSession(@RequestBody String sessionId) {
        System.out.println("DEBUG " + sessionId);
        if (authenticationApplicationService.checkValidity(sessionId)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
