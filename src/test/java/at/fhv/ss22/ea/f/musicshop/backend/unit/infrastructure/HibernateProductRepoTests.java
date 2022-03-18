package at.fhv.ss22.ea.f.musicshop.backend.unit.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Song;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HibernateProductRepoTests {

    private ProductRepository productRepository = new HibernateProductRepository();

    @Test
    void given_product_when_search_by_id() {
        Product product = Product.create(new ProductId(UUID.randomUUID()), "albumA", "2000", List.of("Rock"), "label1", "3:00", List.of(new ArtistId(UUID.randomUUID())), List.of(Song.create("songA", "3:00")));
        productRepository.add(product);

        //when
        Optional<Product> productOpt = productRepository.productById(product.getId());
        //then
        assertTrue(productOpt.isPresent());
        Product p = productOpt.get();
        assertEquals(product.getId(), p.getId());
        assertEquals(product.getName(), p.getName());
    }
}
