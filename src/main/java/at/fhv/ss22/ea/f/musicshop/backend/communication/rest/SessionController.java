package at.fhv.ss22.ea.f.musicshop.backend.communication.rest;

import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("validateSession")
public class SessionController {
    private static final Logger logger = LogManager.getLogger(SessionController.class);

    @EJB
    private AuthenticationApplicationService authenticationApplicationService;


    //not really resource oriented api, but couldn't come up with a "validate" resource
    @GET
    @APIResponse(responseCode = "200", description = "valid session")
    @APIResponse(responseCode = "404", description = "invalid session")
    @Operation(operationId = "validateSession")
    public Response validateSession(@QueryParam("sessionId") String sessionId) {
        if (authenticationApplicationService.checkValidity(sessionId)) {
            logger.info("Verified sessionId {}", sessionId);
            return Response.ok().build();
        } else {
            logger.warn("Failed to valida session {}", sessionId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
