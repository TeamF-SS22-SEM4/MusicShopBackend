package at.fhv.ss22.ea.f.musicshop.backend.domain.repository;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    void add(Product product);

    Optional<Product> productById(ProductId productId);

    List<Product> fullTextSearch(String searchString);
}
