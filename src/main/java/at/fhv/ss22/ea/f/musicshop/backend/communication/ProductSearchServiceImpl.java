package at.fhv.ss22.ea.f.musicshop.backend.communication;

import at.fhv.ss22.ea.f.communication.api.ProductSearchService;
import at.fhv.ss22.ea.f.communication.dto.ProductDetailsDTO;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.ProductApplicationServiceImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class ProductSearchServiceImpl extends UnicastRemoteObject implements ProductSearchService {

    private ProductApplicationService productApplicationService;

    protected ProductSearchServiceImpl() throws RemoteException {
        super();
    }

    public static ProductSearchService newInstance() {
        ProductSearchServiceImpl instance = null;
        try {
            instance = new ProductSearchServiceImpl();
            instance.productApplicationService = ProductApplicationServiceImpl.instance();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public static ProductSearchService newTestInstance(ProductApplicationService productApplicationService) {
        ProductSearchServiceImpl instance = null;
        try {
            instance = new ProductSearchServiceImpl();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        instance.productApplicationService = productApplicationService;
        return instance;
    }

    @Override
    public ProductDetailsDTO productById(UUID productId) {
        return productApplicationService.productById(productId).get();
    }

    @Override
    public List<ProductOverviewDTO> fullTextSearch(String query) {
        return productApplicationService.search(query);
    }
}
