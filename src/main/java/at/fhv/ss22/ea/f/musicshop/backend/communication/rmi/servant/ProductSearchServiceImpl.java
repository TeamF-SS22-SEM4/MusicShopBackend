package at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.servant;

import at.fhv.ss22.ea.f.communication.api.ProductSearchService;
import at.fhv.ss22.ea.f.communication.dto.ProductDetailsDTO;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.RMIServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class ProductSearchServiceImpl extends UnicastRemoteObject implements ProductSearchService {

    private final ProductApplicationService productApplicationService;

    public ProductSearchServiceImpl(ProductApplicationService productApplicationService) throws RemoteException {
        super(RMIServer.getPort());
        this.productApplicationService = productApplicationService;
    }

    @Override
    public ProductDetailsDTO productById(String sessionId, UUID productId) throws SessionExpired, NoPermissionForOperation {
        return productApplicationService.productById("placeholder", productId).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<ProductOverviewDTO> fullTextSearch(String sessionId, String query) throws SessionExpired, NoPermissionForOperation {
        return productApplicationService.search("placeholder", query);
    }
}
