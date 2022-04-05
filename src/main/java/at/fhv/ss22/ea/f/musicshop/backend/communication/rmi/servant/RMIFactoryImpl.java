package at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.servant;

import at.fhv.ss22.ea.f.communication.api.*;
import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIFactoryImpl extends UnicastRemoteObject implements RMIFactory {
    public RMIFactoryImpl(int port) throws RemoteException {
        super(port);
    }

    @Override
    public ProductSearchService getProductSearchService() throws RemoteException {
        return InstanceProvider.getProductSearchService();
    }

    @Override
    public BuyingService getBuyingService() throws RemoteException {
        return InstanceProvider.getBuyingService();
    }

    @Override
    public SaleSearchService getSaleSearchService() throws RemoteException {
        return InstanceProvider.getSaleSearchService();
    }

    @Override
    public RefundSaleService getRefundedSaleService() throws RemoteException {
        return InstanceProvider.getRefundSaleService();
    }

    @Override
    public AuthenticationService getAuthenticationService() throws RemoteException {
        return null;
    }
}
