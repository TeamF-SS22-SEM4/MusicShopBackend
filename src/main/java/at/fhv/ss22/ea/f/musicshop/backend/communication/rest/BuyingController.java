package at.fhv.ss22.ea.f.musicshop.backend.communication.rest;

import at.fhv.ss22.ea.f.communication.dto.SaleDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rest.objects.Purchase;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


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
            @APIResponse(responseCode = "404", description = "unknown carrier id"),
            @APIResponse(responseCode = "400", description = "Payment information invalid")
    })
    @APIResponseSchema(value = String.class, responseCode = "200")
    @Operation(operationId = "placeOrder")
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
            System.out.println("DEBUG " + saleNumber);

            return Response.ok().entity(saleNumber).build();
        } catch (Exception e) {
            System.out.println("DEBUG in catch block");
            return ExceptionHandler.handleException(e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "Ok, list of Purchases retrieved")
    @APIResponse(responseCode = "401", description = "Not Authorized")
    @APIResponse(responseCode = "403", description = "Not Authenticated")
    @APIResponse(responseCode = "404",  description = "User not found")
    @APIResponseSchema(value = SaleDTO[].class, responseCode = "200")
    @Operation(operationId = "getPurchaseHistory")
    public Response getPurchaseHistory(@HeaderParam("session-id") String sessionId) {
        try {
            List<SaleDTO> sales = saleApplicationService.salesByCustomer(sessionId);
            return Response.ok().entity(sales).build();
        } catch(Exception e) {
            return ExceptionHandler.handleException(e);
        }
    }
}