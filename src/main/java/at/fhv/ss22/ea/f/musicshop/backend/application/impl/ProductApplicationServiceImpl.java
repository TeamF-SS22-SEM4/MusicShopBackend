package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.ProductDetailsDTO;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.communication.dto.SongDTO;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.Logged;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RequiresRole;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.SessionKey;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ArtistRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Local(ProductApplicationService.class)
@Stateless
@Logged
public class ProductApplicationServiceImpl implements ProductApplicationService {

    @EJB private ProductRepository productRepository;
    @EJB private ArtistRepository artistRepository;
    @EJB private SoundCarrierRepository soundCarrierRepository;

    public ProductApplicationServiceImpl() {}

    public ProductApplicationServiceImpl(ProductRepository productRepository, ArtistRepository artistRepository, SoundCarrierRepository soundCarrierRepository) {
        this.productRepository = productRepository;
        this.artistRepository = artistRepository;
        this.soundCarrierRepository = soundCarrierRepository;
    }

    @Override
    public ProductDetailsDTO productById(UUID productId) {
        return productRepository.productById(new ProductId(productId)).map(this::detailsDtoFromProduct).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<ProductOverviewDTO> search(String queryString) {
        return this.productRepository.fullTextSearch(queryString).stream()
                .map(this::overviewDtoFromProduct)
                .collect(Collectors.toList());
    }

    private ProductOverviewDTO overviewDtoFromProduct(Product product) {
        List<SoundCarrier> carriers = soundCarrierRepository.soundCarriersByProductId(product.getProductId());
        double smallestPrice = carriers.stream().mapToDouble(SoundCarrier::getPrice).min().orElse(0);

        return ProductOverviewDTO.builder()
                .withId(product.getProductId().getUUID())
                .withName(product.getName())
                .withReleaseYear(product.getReleaseYear())
                .withGenre(String.join(", ", product.getGenre()))
                .withSmallestPrice((float) smallestPrice)
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
                                        .withLocation(carrier.getLocation())
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