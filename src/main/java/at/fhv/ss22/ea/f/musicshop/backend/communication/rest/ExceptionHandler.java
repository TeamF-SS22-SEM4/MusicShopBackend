package at.fhv.ss22.ea.f.musicshop.backend.communication.rest;

import at.fhv.ss22.ea.f.communication.exception.AuthenticationFailed;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;

import javax.ws.rs.core.Response;
import java.util.NoSuchElementException;

public class ExceptionHandler {
    public static Response handleException(Exception exception) {
        Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        if(exception instanceof SessionExpired || exception instanceof AuthenticationFailed) {
            response = Response.status(Response.Status.FORBIDDEN).build();
        } else if (exception instanceof NoPermissionForOperation) {
            response = Response.status(Response.Status.UNAUTHORIZED).build();
        } else if(exception instanceof CarrierNotAvailableException || exception instanceof NoSuchElementException) {
            response = Response.status(Response.Status.NOT_FOUND).build();
        } else if(exception instanceof IllegalArgumentException || exception instanceof javax.ejb.EJBException) {
            // If the IllegalArgumentException is thrown it is stacked with an EJBException
            response = Response.status(Response.Status.BAD_REQUEST).build();
        }

        return response;
    }
}
