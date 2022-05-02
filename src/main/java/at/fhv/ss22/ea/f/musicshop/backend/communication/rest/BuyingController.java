package at.fhv.ss22.ea.f.musicshop.backend.communication.rest;

import at.fhv.ss22.ea.f.musicshop.backend.communication.rest.objects.OrderItem;
import org.hibernate.criterion.Order;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

public class BuyingController {


    @POST
    @Path("order")
    public Response order(@HeaderParam("session-id") String sessionId, List<OrderItem> orderItems) {

        return Response.serverError().build();
    }

}
