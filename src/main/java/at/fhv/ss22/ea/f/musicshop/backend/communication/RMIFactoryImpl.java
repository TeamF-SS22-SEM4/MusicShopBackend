package at.fhv.ss22.ea.f.musicshop.backend.communication;

import at.fhv.ss22.ea.f.communication.api.ProductSearchService;
import at.fhv.ss22.ea.f.communication.api.RMIFactory;
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
}
