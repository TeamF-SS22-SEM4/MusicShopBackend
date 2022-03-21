package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateProductRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ProductApplicationServiceImpl implements ProductApplicationService {
    //implemented this application service as singleton because rmi-remote objects are created for each client
    // if each rmi-instance creates its own "lower" layer-instances, leads to lots of instances of the lower layers
    private static ProductApplicationServiceImpl INSTANCE;
    public static ProductApplicationService instance() {
        if (INSTANCE == null) {
            INSTANCE = new ProductApplicationServiceImpl();
            INSTANCE.productRepository = new HibernateProductRepository();
        }
        return INSTANCE;
    }
    public static ProductApplicationService newTestInstance(ProductRepository productRepository) {
        ProductApplicationServiceImpl testInstance = new ProductApplicationServiceImpl();
        testInstance.productRepository = productRepository;
        return testInstance;
    }
    private ProductApplicationServiceImpl() {}
    private ProductRepository productRepository;

    @Override
    public List<ProductOverviewDTO> search(String queryString) {
        return this.productRepository.fullTextSearch(queryString).stream()
                .map(product ->
                        ProductOverviewDTO.builder()
                                .withName(product.getName())
                                .withArtistName("Artist") //TODO get artist name from artist repo
                                .withReleaseYear(product.getReleaseYear())
                                .build()
                ).collect(Collectors.toList());
    }
}
