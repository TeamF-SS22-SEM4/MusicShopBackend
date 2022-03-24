package at.fhv.ss22.ea.f.musicshop.backend.communication;

import at.fhv.ss22.ea.f.communication.api.ProductSearchService;
import at.fhv.ss22.ea.f.communication.dto.ProductDetailsDTO;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductApplicationService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class ProductSearchServiceImpl extends UnicastRemoteObject implements ProductSearchService {

    private ProductApplicationService productApplicationService;

    public ProductSearchServiceImpl(ProductApplicationService productApplicationService) throws RemoteException {
        super();
        this.productApplicationService = productApplicationService;
    }

    @Override
    public ProductDetailsDTO productById(UUID productId) {
        return productApplicationService.productById(productId).orElse(null);
    }

    @Override
    public List<ProductOverviewDTO> fullTextSearch(String query) {
        return productApplicationService.search(query);
    }
}
