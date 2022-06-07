package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.communication.dto.ProductDetailsDTO;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.communication.dto.SongDTO;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.ProductApplicationServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.Artist;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Song;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierType;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ArtistRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductApplicationTests {
    private ProductApplicationService productApplicationService;

    private ProductRepository mockedProductRepository = mock(ProductRepository.class);
    private SoundCarrierRepository mockedSoundCarrierRepo = mock(SoundCarrierRepository.class);
    private ArtistRepository mockedArtistRepo = mock(ArtistRepository.class);
//    private AuthenticationApplicationService authenticationApplicationService = mock(AuthenticationApplicationService.class);

    @BeforeAll
    void setup() {
        this.productApplicationService = new ProductApplicationServiceImpl(mockedProductRepository, mockedArtistRepo, mockedSoundCarrierRepo);
//        when(authenticationApplicationService.hasRole(any(), any())).thenReturn(true);
    }

    @Test
    void when_search_product_then_dto_matching_values() throws SessionExpired, NoPermissionForOperation {
        //given
        ArtistId rammsteinId = new ArtistId(UUID.randomUUID());
        Product rosenrot = Product.create(
                new ProductId(UUID.randomUUID()),
                "Rosenrot",
                "2000",
                List.of("Rock", "Metal"),
                "Rammstein GBR",
                "40:00",
                List.of(rammsteinId),
                List.of(
                        Song.create("Benzin", "3:46"),
                        Song.create("Mann gegen Mann", "3:00"),
                        Song.create("Rosenrot", "3:00"),
                        Song.create("Spring", "3:00"),
                        Song.create("Wo bist du", "3:00"),
                        Song.create("Stirb nicht vor mir!", "3:00")
                )
        );
        Artist rammstein = Artist.create(
                rammsteinId,
                "rammstein",
                "deutschland",
                List.of(rosenrot.getProductId())
        );

        List<Product> products = new ArrayList<>();
        products.add(rosenrot);

        when(mockedProductRepository.fullTextSearch(anyString())).thenReturn(products);
        when(mockedArtistRepo.artistById(rammsteinId)).thenReturn(Optional.of(rammstein));

        //when
        List<ProductOverviewDTO> productDTOs = productApplicationService.search("irrelevant to this test", 0);


        //then
        assertEquals(1, productDTOs.size());
        ProductOverviewDTO dto = productDTOs.get(0);

        assertEquals(rosenrot.getArtistIds().size(), dto.getArtistName().split(",").length);
        assertEquals(rosenrot.getName(), dto.getName());
    }

    @Test
    void given_product_artist_and_carrier_when_get_product_by_id_for_then_dto_values_correspond_to_model_values() throws SessionExpired, NoPermissionForOperation {
        //given
        ArtistId rammsteinId = new ArtistId(UUID.randomUUID());
        Product rosenrot = Product.create(
                    new ProductId(UUID.randomUUID()),
                    "Rosenrot",
                    "2000",
                    List.of("Rock", "Metal"),
                    "Rammstein GBR",
                    "40:00",
                    List.of(rammsteinId),
                    List.of(
                            Song.create("Benzin", "3:46"),
                            Song.create("Mann gegen Mann", "3:00"),
                            Song.create("Rosenrot", "3:00"),
                            Song.create("Spring", "3:00"),
                            Song.create("Wo bist du", "3:00"),
                            Song.create("Stirb nicht vor mir!", "3:00")
                    )
        );
        Artist rammstein = Artist.create(
                rammsteinId,
                "rammstein",
                "deutschland",
                List.of(rosenrot.getProductId())
                );
        List<SoundCarrier> soundCarriers = List.of(
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 14f, 6, "A7", rosenrot.getProductId()),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.CD, 10f, 100, "B6", rosenrot.getProductId())
        );
        when(mockedProductRepository.productById(rosenrot.getProductId())).thenReturn(Optional.of(rosenrot));
        when(mockedArtistRepo.artistById(rosenrot.getArtistIds().get(0))).thenReturn(Optional.of(rammstein));
        when(mockedSoundCarrierRepo.soundCarriersByProductId(rosenrot.getProductId())).thenReturn(soundCarriers);

        //when
        ProductDetailsDTO product = productApplicationService.productById(rosenrot.getProductId().getUUID());


        //then
        assertEquals(rosenrot.getProductId().getUUID(), product.getProductId());
        assertEquals(rosenrot.getName(), product.getName());
        assertEquals(rosenrot.getLabel(), product.getLabelName());
        assertEquals(rosenrot.getReleaseYear(), product.getReleaseYear());

        List<SongDTO> songs = product.getSongs();
        assertTrue(
                songs.stream().map(SongDTO::getTitle).allMatch(
                        title -> rosenrot.getSongs().stream().map(Song::getTitle).collect(Collectors.toList()).contains(title)
                )
        );
        assertTrue(product.getArtistName().contains(rammstein.getArtistName()));

        List<SoundCarrierDTO> carrierDTOS = product.getSoundCarriers();
        assertEquals(soundCarriers.size(), carrierDTOS.size());

    }

    @Test
    void when_page_number_equals_zero_then_return_whole_list() throws SessionExpired, NoPermissionForOperation{
        //given
        ArtistId rammsteinId = new ArtistId(UUID.randomUUID());

        List<Product> products = new ArrayList<>();

        for(int i = 1; i <= 100; i++){
            Product p = Product.create(
                    new ProductId(UUID.randomUUID()),
                    "Rosenrot"+i,
                    "2000"+i,
                    List.of("Rock", "Metal"+i),
                    "Rammstein GBR"+i,
                    "40:00"+i,
                    List.of(rammsteinId),
                    List.of(
                            Song.create("Benzin"+i, "3:46"+i),
                            Song.create("Mann gegen Mann"+i, "3:00"+i),
                            Song.create("Rosenrot"+i, "3:00"+i),
                            Song.create("Spring"+i, "3:00"+i),
                            Song.create("Wo bist du"+i, "3:00"+i),
                            Song.create("Stirb nicht vor mir!"+i, "3:00"+i)
                    )
            );
            products.add(p);
        }

        List<ProductId> productIds = new ArrayList<>();

        for(int i = 0; i < products.size(); i++){
            productIds.add(products.get(i).getProductId());
        }

        Artist rammstein = Artist.create(
                rammsteinId,
                "rammstein",
                "deutschland",
                productIds
        );
        when(mockedProductRepository.fullTextSearch(anyString())).thenReturn(products);
        when(mockedArtistRepo.artistById(rammsteinId)).thenReturn(Optional.of(rammstein));

        //when
        List<ProductOverviewDTO> productDTOs = productApplicationService.search("irrelevant to this test", 0);

        //then
        assertEquals(products.size(), productDTOs.size());
    }

    @Test
    void when_page_number_equals_two_and_two_exists_then_return_two() throws SessionExpired, NoPermissionForOperation {
        //given
        ArtistId rammsteinId = new ArtistId(UUID.randomUUID());

        List<Product> products = new ArrayList<>();

        for(int i = 1; i <= 100; i++){
            Product p = Product.create(
                    new ProductId(UUID.randomUUID()),
                    "Rosenrot"+i,
                    "2000"+i,
                    List.of("Rock", "Metal"+i),
                    "Rammstein GBR"+i,
                    "40:00"+i,
                    List.of(rammsteinId),
                    List.of(
                            Song.create("Benzin"+i, "3:46"+i),
                            Song.create("Mann gegen Mann"+i, "3:00"+i),
                            Song.create("Rosenrot"+i, "3:00"+i),
                            Song.create("Spring"+i, "3:00"+i),
                            Song.create("Wo bist du"+i, "3:00"+i),
                            Song.create("Stirb nicht vor mir!"+i, "3:00"+i)
                    )
            );
            products.add(p);
        }

        products.sort(Comparator.comparing(Product::getName));

        List<ProductId> productIds = new ArrayList<>();

        for(int i = 0; i < products.size(); i++){
            productIds.add(products.get(i).getProductId());
        }

        Artist rammstein = Artist.create(
                rammsteinId,
                "rammstein",
                "deutschland",
                productIds
        );
        when(mockedProductRepository.fullTextSearch(anyString())).thenReturn(products);
        when(mockedArtistRepo.artistById(rammsteinId)).thenReturn(Optional.of(rammstein));

        List<Product> pageTwo = new ArrayList<>();

        int start = 20;
        int end = 39;

        for(int i = start; i <= end; i++){
            pageTwo.add(products.get(i));
        }

        //when
        List<ProductOverviewDTO> productDTOs = productApplicationService.search("irrelevant to this test", 2);
        //then
        assertEquals(pageTwo.size(), productDTOs.size());

        for(int i = 0; i < pageTwo.size(); i++) {
            assertEquals(pageTwo.get(i).getProductId().getUUID(), productDTOs.get(i).getProductId());
        }
    }

    @Test
    void when_page_number_equals_ten_and_ten_does_not_exists_then_return_empty_list() throws SessionExpired, NoPermissionForOperation {
        //given
        ArtistId rammsteinId = new ArtistId(UUID.randomUUID());

        List<Product> products = new ArrayList<>();

        for(int i = 1; i <= 100; i++){
            Product p = Product.create(
                    new ProductId(UUID.randomUUID()),
                    "Rosenrot"+i,
                    "2000"+i,
                    List.of("Rock", "Metal"+i),
                    "Rammstein GBR"+i,
                    "40:00"+i,
                    List.of(rammsteinId),
                    List.of(
                            Song.create("Benzin"+i, "3:46"+i),
                            Song.create("Mann gegen Mann"+i, "3:00"+i),
                            Song.create("Rosenrot"+i, "3:00"+i),
                            Song.create("Spring"+i, "3:00"+i),
                            Song.create("Wo bist du"+i, "3:00"+i),
                            Song.create("Stirb nicht vor mir!"+i, "3:00"+i)
                    )
            );
            products.add(p);
        }

        List<ProductId> productIds = new ArrayList<>();

        for(int i = 0; i < products.size(); i++){
            productIds.add(products.get(i).getProductId());
        }

        Artist rammstein = Artist.create(
                rammsteinId,
                "rammstein",
                "deutschland",
                productIds
        );
        when(mockedProductRepository.fullTextSearch(anyString())).thenReturn(products);
        when(mockedArtistRepo.artistById(rammsteinId)).thenReturn(Optional.of(rammstein));

        int expectedSize = 0;

        //when
        List<ProductOverviewDTO> productDTOs = productApplicationService.search("irrelevant to this test", 10);
        //then
        assertEquals(expectedSize, productDTOs.size());
    }

    @Test
    void when_last_page_has_only_10_and_less_than_PAGE_SIZE_then_return_10() throws SessionExpired, NoPermissionForOperation {
        //given
        ArtistId rammsteinId = new ArtistId(UUID.randomUUID());

        List<Product> products = new ArrayList<>();

        for(int i = 1; i <= 110; i++){
            Product p = Product.create(
                    new ProductId(UUID.randomUUID()),
                    "Rosenrot"+i,
                    "2000"+i,
                    List.of("Rock", "Metal"+i),
                    "Rammstein GBR"+i,
                    "40:00"+i,
                    List.of(rammsteinId),
                    List.of(
                            Song.create("Benzin"+i, "3:46"+i),
                            Song.create("Mann gegen Mann"+i, "3:00"+i),
                            Song.create("Rosenrot"+i, "3:00"+i),
                            Song.create("Spring"+i, "3:00"+i),
                            Song.create("Wo bist du"+i, "3:00"+i),
                            Song.create("Stirb nicht vor mir!"+i, "3:00"+i)
                    )
            );
            products.add(p);
        }

        List<ProductId> productIds = new ArrayList<>();

        for(int i = 0; i < products.size(); i++){
            productIds.add(products.get(i).getProductId());
        }

        Artist rammstein = Artist.create(
                rammsteinId,
                "rammstein",
                "deutschland",
                productIds
        );
        when(mockedProductRepository.fullTextSearch(anyString())).thenReturn(products);
        when(mockedArtistRepo.artistById(rammsteinId)).thenReturn(Optional.of(rammstein));

        int expectedSize = 10;

        //when
        List<ProductOverviewDTO> productDTOs = productApplicationService.search("irrelevant to this test", 6);
        //then
        assertEquals(expectedSize, productDTOs.size());
    }
}
