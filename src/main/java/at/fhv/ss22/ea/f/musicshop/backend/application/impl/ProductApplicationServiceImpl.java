package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.ProductDetailsDTO;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.communication.dto.SongDTO;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.Logged;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ArtistRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.*;
import java.util.stream.Collectors;

@Local(ProductApplicationService.class)
@Stateless
@Logged
public class ProductApplicationServiceImpl implements ProductApplicationService {
    @EJB private ProductRepository productRepository;
    @EJB private ArtistRepository artistRepository;
    @EJB private SoundCarrierRepository soundCarrierRepository;
    private static final int PAGE_SIZE = 20;

    public ProductApplicationServiceImpl() {}

    public ProductApplicationServiceImpl(ProductRepository productRepository, ArtistRepository artistRepository, SoundCarrierRepository soundCarrierRepository) {
        this.productRepository = productRepository;
        this.artistRepository = artistRepository;
        this.soundCarrierRepository = soundCarrierRepository;
    }

    @Override
    public ProductDetailsDTO productById(UUID productId) throws NoSuchElementException {
        return productRepository.productById(new ProductId(productId)).map(this::detailsDtoFromProduct).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<ProductOverviewDTO> search(String queryString, int pageNumber) {
        //only used to get the length of the list
        List<Product> allProducts =  this.productRepository.fullTextSearch(queryString);

        // Sort list before splitting it up in pages
        allProducts.sort(Comparator.comparing(Product::getName));

        int start = (pageNumber - 1) * PAGE_SIZE;
        int end = (start + PAGE_SIZE) - 1;

        List<Product> pagedProducts = new ArrayList<>();

        if(pageNumber == 0){
            return allProducts.stream().map(this::overviewDtoFromProduct)
                    .collect(Collectors.toList());
        } else if (allProducts.size() - start < PAGE_SIZE) {
            int productsLeft = allProducts.size() - start;
            for(int i = start; i < (start + productsLeft); i++) {
                pagedProducts.add(allProducts.get(i));
            }

            return pagedProducts.stream().map(this::overviewDtoFromProduct)
                    .collect(Collectors.toList());
        } else {
            for(int i = start; i <= end; i++){
                pagedProducts.add(allProducts.get(i));
            }

            return pagedProducts.stream().map(this::overviewDtoFromProduct)
                    .collect(Collectors.toList());
        }
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
                                .stream()
                                .sorted(Comparator.comparing(SoundCarrierDTO::getSoundCarrierName))
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