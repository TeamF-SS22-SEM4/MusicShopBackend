package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.communication.dto.ProductDetailsDTO;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.communication.dto.SongDTO;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductApplicationService;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductApplicationTests {

    private ProductApplicationService productApplicationService = InstanceProvider.getTestingProductApplicationService();

    private ProductRepository mockedProductRepository = InstanceProvider.getMockedProductRepository();
    private SoundCarrierRepository mockedSoundCarrierRepo = InstanceProvider.getMockedSoundCarrierRepository();
    private ArtistRepository mockedArtistRepo = InstanceProvider.getMockedArtistRepository();
    private AuthenticationApplicationService authenticationApplicationService = InstanceProvider.getMockedAuthenticationApplicationService();

    @BeforeAll
    void setup() throws SessionExpired {
        when(authenticationApplicationService.hasRole(any(), any())).thenReturn(true);
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
        when(mockedProductRepository.fullTextSearch(anyString())).thenReturn(List.of(rosenrot));
        when(mockedArtistRepo.artistById(rammsteinId)).thenReturn(Optional.of(rammstein));

        //when
        List<ProductOverviewDTO> productDTOs = productApplicationService.search("placeholder", "irrelevant to this test");


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
        Optional<ProductDetailsDTO> productOpt = productApplicationService.productById("placeholder", rosenrot.getProductId().getUUID());


        //then
        assertTrue(productOpt.isPresent());
        ProductDetailsDTO product = productOpt.get();
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
}
