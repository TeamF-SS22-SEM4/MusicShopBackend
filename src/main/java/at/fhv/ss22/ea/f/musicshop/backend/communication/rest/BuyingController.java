package at.fhv.ss22.ea.f.musicshop.backend.communication.rest;

import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rest.objects.Purchase;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;


@Path("/orders")
public class BuyingController {
    @EJB
    private SaleApplicationService saleApplicationService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Ok, placed order"),
            @APIResponse(responseCode = "403", description = "Not Authenticated"),
            @APIResponse(responseCode = "401", description = "Unauthorized for operation"),
            @APIResponse(responseCode = "404", description = "unknown carrier id")
    })
    @Operation(operationId = "placeOrder")
    //TODO maybe extract sessionId (also from application)
    public Response placeOrder(@HeaderParam("session-id") String sessionId, @RequestBody Purchase purchase) {
        try {
            String saleNumber = saleApplicationService.buyAsCustomer(
                    sessionId,
                    Arrays.asList(purchase.getOrderItems()),
                    purchase.getPaymentInformation().getPaymentMethod(),
                    purchase.getPaymentInformation().getCreditCardType(),
                    purchase.getPaymentInformation().getCreditCardNumber(),
                    purchase.getPaymentInformation().getCvc()
            );

            return Response.ok().entity(saleNumber).build();
        } catch (Exception e) {
            return ExceptionHandler.handleException(e);
        }
    }
}