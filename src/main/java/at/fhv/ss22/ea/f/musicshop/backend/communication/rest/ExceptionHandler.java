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

        // Sometimes the exception is wrapped by javax.ejb.EJBException and contains the real exception as message
        if(exception instanceof javax.ejb.EJBException) {
            String exceptionAsMsg = exception.getMessage();
            if(exceptionAsMsg.equals("at.fhv.ss22.ea.f.communication.exception.SessionExpired") ||
                    exceptionAsMsg.equals("at.fhv.ss22.ea.f.communication.exception.AuthenticationFailed")) {
                response = Response.status(Response.Status.FORBIDDEN).build();
            } else if(exceptionAsMsg.equals("at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation")) {
                response = Response.status(Response.Status.UNAUTHORIZED).build();
            } else if(exceptionAsMsg.equals("java.util.NoSuchElementException") ||
                    exceptionAsMsg.equals("at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException")) {
                response = Response.status(Response.Status.NOT_FOUND).build();
            } else if(exceptionAsMsg.equals("java.lang.IllegalArgumentException")) {
                response = Response.status(Response.Status.BAD_REQUEST).build();
            }
        }

        if(exception instanceof SessionExpired || exception instanceof AuthenticationFailed) {
            response = Response.status(Response.Status.FORBIDDEN).build();
        } else if (exception instanceof NoPermissionForOperation) {
            response = Response.status(Response.Status.UNAUTHORIZED).build();
        } else if(exception instanceof NoSuchElementException || exception instanceof CarrierNotAvailableException) {
            response = Response.status(Response.Status.NOT_FOUND).build();
        } else if(exception instanceof IllegalArgumentException) {
            response = Response.status(Response.Status.BAD_REQUEST).build();
        }

        return response;
    }
}
