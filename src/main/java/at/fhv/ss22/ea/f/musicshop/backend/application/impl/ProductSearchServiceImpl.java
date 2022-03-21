package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductOverviewDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductSearchService;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateProductRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ProductSearchServiceImpl implements ProductSearchService {

    private ProductRepository productRepository;

    public static ProductSearchService newInstance() {
        ProductSearchServiceImpl instance = new ProductSearchServiceImpl();
        instance.productRepository = new HibernateProductRepository();
        return instance;
    }

    public static ProductSearchService newTestInstance(ProductRepository productRepository) {
        ProductSearchServiceImpl instance = new ProductSearchServiceImpl();
        instance.productRepository = productRepository;
        return instance;
    }

    private ProductSearchServiceImpl() {
    }

    @Override
    public List<ProductOverviewDTO> fullTextSearch(String query) {
        return this.productRepository.fullTextSearch(query).stream()
                .map(product -> new ProductOverviewDTO(product.getName())).collect(Collectors.toList());
    }
}
