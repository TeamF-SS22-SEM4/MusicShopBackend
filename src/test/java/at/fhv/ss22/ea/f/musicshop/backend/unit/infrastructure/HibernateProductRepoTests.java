package at.fhv.ss22.ea.f.musicshop.backend.unit.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Song;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateProductRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HibernateProductRepoTests {

    private ProductRepository productRepository = new HibernateProductRepository();

    @Test
    void given_product_when_searched_by_equal_but_not_same_id_then_product_found() {
        Product product = Product.create(new ProductId(UUID.randomUUID()), "albumA", "2000", List.of("Rock"), "label1", "3:00", List.of(new ArtistId(UUID.randomUUID())), List.of(Song.create("songA", "3:00")));
        productRepository.add(product);
        ProductId surrogateId = new ProductId(product.getProductId().getUUID());

        //when
        Optional<Product> productOpt = productRepository.productById(surrogateId);
        //then
        assertTrue(productOpt.isPresent());
        Product p = productOpt.get();
        assertEquals(product.getProductId(), p.getProductId());
        assertEquals(product.getName(), p.getName());
        assertTrue(productOpt.get().getSongs().containsAll(product.getSongs()));
    }

    @Test
    void given_invalid_product_id_when_searched_then_empty_result() {
        //given
        ProductId id = new ProductId(UUID.randomUUID());

        //when
        Optional<Product> productOpt = productRepository.productById(id);

        //then
        assertTrue(productOpt.isEmpty());
    }
}
