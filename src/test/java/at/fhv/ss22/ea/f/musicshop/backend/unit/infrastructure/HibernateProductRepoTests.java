package at.fhv.ss22.ea.f.musicshop.backend.unit.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.Artist;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Song;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ArtistRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateArtistRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateProductRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HibernateProductRepoTests {

    private ArtistRepository artistRepository = new HibernateArtistRepository();

    private ProductRepository productRepository = new HibernateProductRepository();

    @Test
    void given_product_when_searched_by_equal_but_not_same_id_then_product_found() {
        Product product = Product.create(new ProductId(UUID.randomUUID()), "albumA", "2000", List.of("Rock"), "label1", "3:00", List.of(new ArtistId(UUID.randomUUID())), List.of(Song.create("songA", "3:00")));
        EntityManagerUtil.beginTransaction();
        productRepository.add(product);
        EntityManagerUtil.commit();
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

    @Test
    void full_text_search_test_with_multiple_keywords_album_names_case_insensitive() {
        //given
        Product courrete1 = Product.create(new ProductId(UUID.randomUUID()), "We are The Courettes", "2022", List.of("Rock"), "Damaged Goods", "40:00", List.of(new ArtistId(UUID.randomUUID())), List.of(Song.create("Hoodoo Hop", "3:00"), Song.create("Time Is Ticking", "3:00")));
        Product courrete2 = Product.create(new ProductId(UUID.randomUUID()), "Here are The Courettes", "2022", List.of("Rock"), "Damaged Goods", "40:00", List.of(new ArtistId(UUID.randomUUID())), List.of(Song.create("I've been Walking", "3:00"), Song.create("Go! Go! Go!", "3:00")));
        Product sabbaton = Product.create(new ProductId(UUID.randomUUID()), "The War To End All Wars", "2022", List.of("Rock"), "Soyuz Music", "40:00", List.of(new ArtistId(UUID.randomUUID())), List.of(Song.create("Sarajevo", "3:00"), Song.create("Stormtroopers", "3:00")));
        Product bridges = Product.create(new ProductId(UUID.randomUUID()), "Texas Moon", "2022", List.of("Rock"), "Dead Oceans", "40:00", List.of(new ArtistId(UUID.randomUUID())), List.of(Song.create("Doris", "3:00"), Song.create("Chocolate Hills", "3:00")));

        EntityManagerUtil.beginTransaction();
        productRepository.add(courrete1);
        productRepository.add(courrete2);
        productRepository.add(sabbaton);
        productRepository.add(bridges);
        EntityManagerUtil.commit();

        //when
        List<Product> productsFound = productRepository.fullTextSearch("coURETTES texas");

        //then
        assertTrue(productsFound.contains(courrete1));
        assertTrue(productsFound.contains(courrete2));
        assertTrue(productsFound.contains(bridges));
        assertFalse(productsFound.contains(sabbaton));
    }

    @Test
    void full_text_search_with_label_name() {
        //given
        Product courrete1 = Product.create(new ProductId(UUID.randomUUID()), "We are The Courettes", "2022", List.of("Rock"), "Damaged Goods", "40:00", List.of(new ArtistId(UUID.randomUUID())), List.of(Song.create("Hoodoo Hop", "3:00"), Song.create("Time Is Ticking", "3:00")));
        Product courrete2 = Product.create(new ProductId(UUID.randomUUID()), "Here are The Courettes", "2022", List.of("Rock"), "Damaged Goods", "40:00", List.of(new ArtistId(UUID.randomUUID())), List.of(Song.create("I've been Walking", "3:00"), Song.create("Go! Go! Go!", "3:00")));
        Product sabbaton = Product.create(new ProductId(UUID.randomUUID()), "The War To End All Wars", "2022", List.of("Rock"), "Soyuz Music", "40:00", List.of(new ArtistId(UUID.randomUUID())), List.of(Song.create("Sarajevo", "3:00"), Song.create("Stormtroopers", "3:00")));
        Product bridges = Product.create(new ProductId(UUID.randomUUID()), "Texas Moon", "2022", List.of("Rock"), "Dead Oceans", "40:00", List.of(new ArtistId(UUID.randomUUID())), List.of(Song.create("Doris", "3:00"), Song.create("Chocolate Hills", "3:00")));

        EntityManagerUtil.beginTransaction();
        productRepository.add(courrete1);
        productRepository.add(courrete2);
        productRepository.add(sabbaton);
        productRepository.add(bridges);
        EntityManagerUtil.commit();

        //when
        List<Product> products = productRepository.fullTextSearch("Ocean MUsic");

        //then
        assertTrue(products.contains(sabbaton));
        assertTrue(products.contains(bridges));
        assertFalse(products.contains(courrete1));
        assertFalse(products.contains(courrete2));
    }

    @Test
    void full_text_search_with_song_title() {
        //given
        Product courrete1 = Product.create(new ProductId(UUID.randomUUID()), "We are The Courettes", "2022", List.of("Rock"), "Damaged Goods", "40:00", List.of(new ArtistId(UUID.randomUUID())), List.of(Song.create("Hoodoo Hop", "3:00"), Song.create("Time Is Ticking", "3:00")));
        Product courrete2 = Product.create(new ProductId(UUID.randomUUID()), "Here are The Courettes", "2022", List.of("Rock"), "Damaged Goods", "40:00", List.of(new ArtistId(UUID.randomUUID())), List.of(Song.create("I've been Walking", "3:00"), Song.create("Go! Go! Go!", "3:00")));
        Product sabbaton = Product.create(new ProductId(UUID.randomUUID()), "The War To End All Wars", "2022", List.of("Rock"), "Soyuz Music", "40:00", List.of(new ArtistId(UUID.randomUUID())), List.of(Song.create("Sarajevo", "3:00"), Song.create("Stormtroopers", "3:00")));
        Product bridges = Product.create(new ProductId(UUID.randomUUID()), "Texas Moon", "2022", List.of("Rock"), "Dead Oceans", "40:00", List.of(new ArtistId(UUID.randomUUID())), List.of(Song.create("Doris", "3:00"), Song.create("Chocolate Hills", "3:00")));

        EntityManagerUtil.beginTransaction();
        productRepository.add(courrete1);
        productRepository.add(courrete2);
        productRepository.add(sabbaton);
        productRepository.add(bridges);
        EntityManagerUtil.commit();

        //when
        List<Product> products = productRepository.fullTextSearch("walking STORM");

        //then
        assertFalse(products.contains(bridges));
        assertFalse(products.contains(courrete1));
        assertTrue(products.contains(sabbaton));
        assertTrue(products.contains(courrete2));
    }

    @Test
    void full_text_search_with_artist_name() {
        //given
        ArtistId courreteId = new ArtistId(UUID.randomUUID());
        ArtistId sabbatonId = new ArtistId(UUID.randomUUID());
        ArtistId bridgesId = new ArtistId(UUID.randomUUID());
        Product courrete1 = Product.create(new ProductId(UUID.randomUUID()), "We are The Courettes", "2022", List.of("Rock"), "Damaged Goods", "40:00", List.of(courreteId), List.of(Song.create("Hoodoo Hop", "3:00"), Song.create("Time Is Ticking", "3:00")));
        Product courrete2 = Product.create(new ProductId(UUID.randomUUID()), "Here are The Courettes", "2022", List.of("Rock"), "Damaged Goods", "40:00", List.of(courreteId), List.of(Song.create("I've been Walking", "3:00"), Song.create("Go! Go! Go!", "3:00")));
        Product sabbaton1 = Product.create(new ProductId(UUID.randomUUID()), "The War To End All Wars", "2022", List.of("Rock"), "Soyuz Music", "40:00", List.of(sabbatonId), List.of(Song.create("Sarajevo", "3:00"), Song.create("Stormtroopers", "3:00")));
        Product bridges1 = Product.create(new ProductId(UUID.randomUUID()), "Texas Moon", "2022", List.of("Rock"), "Dead Oceans", "40:00", List.of(bridgesId), List.of(Song.create("Doris", "3:00"), Song.create("Chocolate Hills", "3:00")));
        Artist courretes = Artist.create(courreteId, "The courrettes", "england", List.of(courrete1.getProductId(), courrete2.getProductId()));
        Artist sabbaton = Artist.create(sabbatonId, "Sabaton", "england", List.of(sabbaton1.getProductId()));
        Artist bridges = Artist.create(bridgesId, "The Bridges", "england", List.of(bridges1.getProductId()));

        EntityManagerUtil.beginTransaction();
        artistRepository.add(courretes);
        artistRepository.add(sabbaton);
        artistRepository.add(bridges);
        productRepository.add(courrete1);
        productRepository.add(courrete2);
        productRepository.add(sabbaton1);
        productRepository.add(bridges1);
        EntityManagerUtil.commit();

        //when
        List<Product> products = productRepository.fullTextSearch("Sabat");

        //then
        assertTrue(products.contains(sabbaton1));
        assertFalse(products.contains(courrete2));
        assertFalse(products.contains(courrete1));
        assertFalse(products.contains(bridges1));
    }
}
