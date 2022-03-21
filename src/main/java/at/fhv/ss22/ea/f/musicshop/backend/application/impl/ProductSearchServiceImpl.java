package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.api.ProductSearchService;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateProductRepository;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ProductSearchServiceImpl extends UnicastRemoteObject implements ProductSearchService {

    private ProductRepository productRepository;

    protected ProductSearchServiceImpl() throws RemoteException {
        super();
    }

    public static ProductSearchService newInstance() throws RemoteException {
        ProductSearchServiceImpl instance = new ProductSearchServiceImpl();
        // Caused by: java.lang.ClassNotFoundException: org.hibernate.service.spi.ServiceException (no security manager: RMI class loader disabled)
        //instance.productRepository = new HibernateProductRepository();
        return instance;
    }

    public static ProductSearchService newTestInstance(ProductRepository productRepository) throws RemoteException {
        ProductSearchServiceImpl instance = new ProductSearchServiceImpl();
        instance.productRepository = productRepository;
        return instance;
    }

    @Override
    public List<ProductOverviewDTO> fullTextSearch(String query) {
        /*
        return this.productRepository.fullTextSearch(query).stream()
                .map(product ->
                        ProductOverviewDTO.builder()
                                .withName(product.getName())
                                .withArtistName("Artist")
                                .withReleaseYear(product.getReleaseYear())
                                .build()
                ).collect(Collectors.toList());
         */
        // Fake List to test RMI
        List<ProductOverviewDTO> productOverviewDTOs = new ArrayList<>();

        for(int i = 1; i <= 20; i++) {
            productOverviewDTOs.add(
                    ProductOverviewDTO.builder()
                            .withName("Product " + i)
                            .withArtistName("Artist " + i)
                            .withReleaseYear("1980")
                            .build()
            );
        }

        return productOverviewDTOs;
    }
}
