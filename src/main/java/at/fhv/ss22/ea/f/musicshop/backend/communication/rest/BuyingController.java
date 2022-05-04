package at.fhv.ss22.ea.f.musicshop.backend.communication.rest;

import at.fhv.ss22.ea.f.communication.dto.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rest.objects.OrderItem;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rest.objects.PaymentInformation;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rest.objects.Purchase;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.rmi.RemoteException;
import java.util.Arrays;


@Path("/orders")
public class BuyingController {
    @EJB
    private SaleApplicationService saleApplicationService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
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
        } catch (SessionExpired | CarrierNotAvailableException | RemoteException | NoPermissionForOperation e) {
            return ExceptionHandler.handleException(e);
        }
    }
}