package at.fhv.ss22.ea.f.musicshop.backend;

import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductSearchService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.ProductSearchServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Song;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Application {
    public static void main(String[] args) {
        ProductSearchService searchService = ProductSearchServiceImpl.newInstance();
        searchService.fullTextSearch("tom").forEach(dto -> System.out.println(dto.toString()));
    }
}
