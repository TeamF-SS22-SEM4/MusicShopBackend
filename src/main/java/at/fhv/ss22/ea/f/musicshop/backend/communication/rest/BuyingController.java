package at.fhv.ss22.ea.f.musicshop.backend.communication.rest;

import at.fhv.ss22.ea.f.communication.dto.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rest.objects.PaymentInformation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

@Path("/orders")
public class BuyingController {
    @EJB
    private SaleApplicationService saleApplicationService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response placeOrder(@HeaderParam("session-id") String sessionId, @RequestBody SoundCarrierAmountDTO[] orderItems, @RequestBody PaymentInformation paymentInformation) {
        try {
            String saleNumber = saleApplicationService.buyAsCustomer(
                    sessionId,
                    Arrays.asList(orderItems),
                    paymentInformation.getPaymentMethod(),
                    paymentInformation.getCreditCardType(),
                    paymentInformation.getCreditCardNumber(),
                    paymentInformation.getCvc()
            );

            return Response.ok().entity(saleNumber).build();
        } catch (SessionExpired e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        } catch (NoPermissionForOperation e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (RemoteException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (CarrierNotAvailableException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}