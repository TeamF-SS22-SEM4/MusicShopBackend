package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.ProductDetailsDTO;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.communication.dto.SongDTO;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ArtistRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateArtistRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateSoundCarrierRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductApplicationServiceImpl implements ProductApplicationService {
    //implemented this application service as singleton because rmi-remote objects are created for each client
    // if each rmi-instance creates its own "lower" layer-instances, leads to lots of instances of the lower layers
    private static ProductApplicationServiceImpl INSTANCE;
    public static ProductApplicationService instance() {
        if (INSTANCE == null) {
            INSTANCE = new ProductApplicationServiceImpl();
            INSTANCE.productRepository = new HibernateProductRepository();
            INSTANCE.artistRepository = new HibernateArtistRepository();
            INSTANCE.soundCarrierRepository = new HibernateSoundCarrierRepository();
        }
        return INSTANCE;
    }
    public static ProductApplicationService newTestInstance(
            ProductRepository productRepository,
            ArtistRepository artistRepository,
            SoundCarrierRepository soundCarrierRepository
    ) {
        ProductApplicationServiceImpl testInstance = new ProductApplicationServiceImpl();
        testInstance.productRepository = productRepository;
        testInstance.artistRepository = artistRepository;
        testInstance.soundCarrierRepository = soundCarrierRepository;
        return testInstance;
    }
    private ProductApplicationServiceImpl() {}
    private ProductRepository productRepository;
    private ArtistRepository artistRepository;
    private SoundCarrierRepository soundCarrierRepository;


    @Override
    public Optional<ProductDetailsDTO> productById(UUID productId) {
        Optional<Product> productOpt = productRepository.productById(new ProductId(productId));
        if (productOpt.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(detailsDtoFromProduct(productOpt.get()));
    }

    @Override
    public List<ProductOverviewDTO> search(String queryString) {
        return this.productRepository.fullTextSearch(queryString).stream()
                .map(this::overviewDtoFromProduct)
                .collect(Collectors.toList());
    }

    //TODO maybe extract to DTOassembler
    private ProductOverviewDTO overviewDtoFromProduct(Product product) {
        return ProductOverviewDTO.builder()
                .withName(product.getName())
                .withReleaseYear(product.getReleaseYear())
                .withArtistName(product.getArtistIds().stream()
                        .map(artistId -> artistRepository.artistById(artistId))
                        .filter(Optional::isPresent)
                        .map(opt -> opt.get().getArtistName())
                        .collect(Collectors.joining(", ")))
                .build();
    }

    private ProductDetailsDTO detailsDtoFromProduct(Product product) {
        return ProductDetailsDTO.builder()
                .withId(product.getProductId().getUUID())
                .withName(product.getName())
                .withReleaseYear(product.getReleaseYear())
                .withDuration(product.getDuration())
                .withLabelName(product.getLabel())
                .withSongs(product.getSongs().stream()
                        .map(song -> SongDTO.builder()
                                .withTitle(song.getTitle())
                                .withDuration(song.getDuration())
                                .build())
                        .collect(Collectors.toList()))
                .withSoundCarriers(
                        soundCarrierRepository.soundCarriersByProductId(product.getProductId()).stream()
                                .map(carrier -> SoundCarrierDTO.builder()
                                        .withSoundCarrierName(carrier.getType().getFriendlyName())
                                        .withAvailableAmount(carrier.getAmountInStore())
                                        .withPricePerCarrier(carrier.getPrice())
                                        .build())
                                .collect(Collectors.toList())
                )
                .withArtistName(product.getArtistIds().stream()
                        .map(artistId -> artistRepository.artistById(artistId))
                        .filter(Optional::isPresent)
                        .map(opt -> opt.get().getArtistName())
                        .collect(Collectors.joining(", ")))
                .build();
    }
}