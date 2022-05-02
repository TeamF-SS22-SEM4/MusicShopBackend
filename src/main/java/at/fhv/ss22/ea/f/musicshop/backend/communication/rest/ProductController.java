package at.fhv.ss22.ea.f.musicshop.backend.communication.rest;

import at.fhv.ss22.ea.f.communication.dto.ProductDetailsDTO;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductApplicationService;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Path("/product")
public class ProductController {
    @EJB
    private ProductApplicationService productApplicationService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response productById(@HeaderParam("session-id") String sessionId, @PathParam("id") String productId) {
        try {
            ProductDetailsDTO product = productApplicationService.productById(sessionId, UUID.fromString(productId));
            return Response.ok().entity(product).build();
        } catch (SessionExpired e) {
            return Response.status(401).build();
        } catch (NoPermissionForOperation e) {
            return Response.status(403).build();
        } catch (NoSuchElementException e) {
            return Response.status(400).entity("Product with id " + productId + " not found").build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response search(@HeaderParam("session-id") String sessionId, @QueryParam("search") @DefaultValue("") String query) {
        try {
            List<ProductOverviewDTO> products = productApplicationService.search(sessionId, query);
            return Response.ok().entity(products).build();
        } catch (SessionExpired e) {
            return Response.status(401).build();
        } catch (NoPermissionForOperation e) {
            return Response.status(403).build();
        }
    }
}
