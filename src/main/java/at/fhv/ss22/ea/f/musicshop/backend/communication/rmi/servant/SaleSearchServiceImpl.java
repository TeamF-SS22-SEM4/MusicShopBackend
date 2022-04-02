package at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.servant;

import at.fhv.ss22.ea.f.communication.api.SaleSearchService;
import at.fhv.ss22.ea.f.communication.dto.SaleDTO;
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
    public SaleDTO saleByInvoiceNumber(String invoiceNumber) throws RemoteException {
        return saleApplicationService.saleByInvoiceNumber(invoiceNumber).orElseThrow(NoSuchElementException::new);
    }
}
