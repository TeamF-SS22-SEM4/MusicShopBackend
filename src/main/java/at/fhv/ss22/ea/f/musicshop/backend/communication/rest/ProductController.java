package at.fhv.ss22.ea.f.musicshop.backend.communication.rest;

import at.fhv.ss22.ea.f.communication.dto.ProductDetailsDTO;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductApplicationService;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Path("/products")
public class ProductController {
    @EJB
    private ProductApplicationService productApplicationService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponses({
            @APIResponse(responseCode = "403", description = "Not Authenticated"),
            @APIResponse(responseCode = "401", description = "Unauthorized for operation"),
            @APIResponse(responseCode = "404", description = "unknown product id")
    })
    @APIResponseSchema(value = ProductDetailsDTO.class, responseCode = "200")
    //TODO maybe extract sessionId (also from application)
    public Response productById(@HeaderParam("session-id") String sessionId, @PathParam("id") String productId) {
        try {
            ProductDetailsDTO product = productApplicationService.productById(sessionId, UUID.fromString(productId));
            return Response.ok().entity(product).build();

            //TODO move all catches from controller to single location
        } catch (Exception e) {
            return ExceptionHandler.handleException(e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponses({
            @APIResponse(responseCode = "403", description = "Not Authenticated"),
            @APIResponse(responseCode = "401", description = "Unauthorized for operation")
    })
    @APIResponseSchema(value = ProductDetailsDTO[].class, responseCode = "200")
    public Response search(@HeaderParam("session-id") String sessionId, @QueryParam("search") @DefaultValue("") String query) {
        try {
            List<ProductOverviewDTO> products = productApplicationService.search(sessionId, query);
            return Response.ok().entity(products).build();
        } catch (Exception e) {
            return ExceptionHandler.handleException(e);
        }
    }
}
