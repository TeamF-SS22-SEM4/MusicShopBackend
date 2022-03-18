package at.fhv.ss22.ea.f.musicshop.backend;

import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductSearchService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.ProductSearchServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateProductRepository;

public class Application {
    public static void main(String[] args) {
        ProductSearchService searchService = ProductSearchServiceImpl.newInstance();

        searchService.fullTextSearch("tom").forEach(dto -> System.out.println(dto.toString()));

    }
}
