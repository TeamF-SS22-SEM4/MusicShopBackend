package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.ProductDetailsDTO;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.communication.dto.SongDTO;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ArtistRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductApplicationServiceImpl implements ProductApplicationService {

    private ProductRepository productRepository;
    private ArtistRepository artistRepository;
    private SoundCarrierRepository soundCarrierRepository;
    private AuthenticationApplicationService authenticationApplicationService;

    public ProductApplicationServiceImpl(AuthenticationApplicationService authenticationApplicationService, ProductRepository productRepository, ArtistRepository artistRepository, SoundCarrierRepository soundCarrierRepository) {
        this.authenticationApplicationService = authenticationApplicationService;
        this.productRepository = productRepository;
        this.artistRepository = artistRepository;
        this.soundCarrierRepository = soundCarrierRepository;
    }

    @Override
    public Optional<ProductDetailsDTO> productById(String sessionId, UUID productId) throws SessionExpired, NoPermissionForOperation {
        if (!authenticationApplicationService.hasRole(new SessionId(sessionId), UserRole.EMPLOYEE)) {
            throw new NoPermissionForOperation();
        }
        return productRepository.productById(new ProductId(productId)).map(this::detailsDtoFromProduct);
    }

    @Override
    public List<ProductOverviewDTO> search(String sessionId, String queryString) throws SessionExpired, NoPermissionForOperation {
        if (!authenticationApplicationService.hasRole(new SessionId(sessionId), UserRole.EMPLOYEE)) {
            throw new NoPermissionForOperation();
        }
        return this.productRepository.fullTextSearch(queryString).stream()
                .map(this::overviewDtoFromProduct)
                .collect(Collectors.toList());
    }

    private ProductOverviewDTO overviewDtoFromProduct(Product product) {
        return ProductOverviewDTO.builder()
                .withId(product.getProductId().getUUID())
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
                .withGenre(String.join(", ", product.getGenre()))
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
                                        .withSoundCarrierId(carrier.getCarrierId().getUUID())
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