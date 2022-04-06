package at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.servant;

import at.fhv.ss22.ea.f.communication.api.RefundSaleService;
import at.fhv.ss22.ea.f.communication.dto.RefundedSaleItemDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.RMIServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RefundSaleServiceImpl extends UnicastRemoteObject implements RefundSaleService {
    private SaleApplicationService saleApplicationService;

    public RefundSaleServiceImpl(SaleApplicationService saleApplicationService) throws RemoteException {
        super(RMIServer.getPort());
        this.saleApplicationService = saleApplicationService;
    }

    @Override
    public void refundSale(String sessionId, String invoiceNumber, List<RefundedSaleItemDTO> refundedSaleItems) throws RemoteException, SessionExpired, NoPermissionForOperation {
        saleApplicationService.refund("placeholder", invoiceNumber, refundedSaleItems);
    }
}
