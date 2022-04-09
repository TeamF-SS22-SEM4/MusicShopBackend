package at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.servant;

import at.fhv.ss22.ea.f.communication.api.SaleSearchService;
import at.fhv.ss22.ea.f.communication.dto.SaleDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.RMIServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.NoSuchElementException;

public class SaleSearchServiceImpl extends UnicastRemoteObject implements SaleSearchService {

    private SaleApplicationService saleApplicationService;

    public SaleSearchServiceImpl(SaleApplicationService saleApplicationService) throws RemoteException {
        super(RMIServer.getPort());
         this.saleApplicationService = saleApplicationService;
    }

    @Override
    public SaleDTO saleByInvoiceNumber(String sessionId, String invoiceNumber) throws RemoteException, SessionExpired, NoPermissionForOperation {
       return saleApplicationService.saleByInvoiceNumber(sessionId, invoiceNumber).orElseThrow(NoSuchElementException::new);
    }
}
