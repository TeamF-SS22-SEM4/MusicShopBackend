package at.fhv.ss22.ea.f.musicshop.backend.communication.rest;

import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rest.objects.OrderItem;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.hibernate.criterion.Order;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/order")
public class BuyingController {
    @EJB
    private SaleApplicationService saleApplicationService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response order(@HeaderParam("session-id") String sessionId, @RequestBody List<OrderItem> orderItems) {
        return Response.serverError().build();
    }
}