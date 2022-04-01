package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.communication.dto.SaleDTO;
import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.CustomerId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Song;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.Sale;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleItem;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierType;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SaleRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CarrierApplicationTests {

    private SaleApplicationService buyingApplicationService = InstanceProvider.getTestingSoundCarrierApplicationService();
    private SoundCarrierRepository soundCarrierRepository = InstanceProvider.getMockedSoundCarrierRepository();
    private SaleRepository saleRepository = InstanceProvider.getMockedSaleRepository();
    private ProductRepository productRepository = InstanceProvider.getMockedProductRepository();

    @Test
    void sell_carriers() throws CarrierNotAvailableException {
        //given
        List<SoundCarrier> carriers = List.of(
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 20, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 22, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.VINYL, 22, 5, "A1", new ProductId(UUID.randomUUID())),
                SoundCarrier.create(new SoundCarrierId(UUID.randomUUID()), SoundCarrierType.CD, 10, 5, "A1", new ProductId(UUID.randomUUID()))
        );
        for (SoundCarrier s : carriers) {
            when(soundCarrierRepository.soundCarrierById(s.getCarrierId())).thenReturn(Optional.of(s));
        }

        //when
        SoundCarrierAmountDTO buyingDTO = SoundCarrierAmountDTO.builder()
                            .withAmount(2)
                            .withCarrierId(carriers.get(0).getCarrierId().getUUID()).build();
        buyingApplicationService.buy(List.of(buyingDTO), "CASH");

        //then
        assertEquals(3, carriers.get(0).getAmountInStore());
        verify(saleRepository).add(any());
    }

    @Test
    void given_invoiceNumber_when_saleByInvoiceNumber_then_return_matchingSale() {
        // given
        // TODO: Create soundCarrier
        UUID productIdUUID = UUID.randomUUID();
        ProductId productIdExpected = new ProductId(productIdUUID);
        String nameExpected = "SomeProduct";
        String releaseYearExpected = "2020";
        List<String> genresExpected = List.of("Rock", "Pop");
        String labelExpected = "TeamF";
        String durationExpected = "5:00";
        List<ArtistId> artistIdsExpected = List.of(new ArtistId(UUID.randomUUID()), new ArtistId(UUID.randomUUID()));
        List<Song> songsExpected = List.of(Song.create("Song 1", "3:00"), Song.create("Song 2", "2:00"));
        Product product = Product.create(productIdExpected, nameExpected, releaseYearExpected, genresExpected, labelExpected,
                durationExpected, artistIdsExpected, songsExpected);

        UUID soundCarrierIdUUID = UUID.randomUUID();
        SoundCarrierId soundCarrierIdExpected = new SoundCarrierId(soundCarrierIdUUID);
        SoundCarrierType soundCarrierTypeExpected = SoundCarrierType.CD;
        float priceExpected = 15;
        int amountInStoreExpected = 30;
        String locationExpected = "R001";
        SoundCarrier soundCarrier = SoundCarrier.create(soundCarrierIdExpected, soundCarrierTypeExpected, priceExpected,
                                                        amountInStoreExpected, locationExpected, productIdExpected);

        List<SaleItem> saleItemsExpected =  List.of(SaleItem.create(false, 1, 10, soundCarrierIdExpected));
        String invoiceNumberExpected = "42";
        Sale sale = Sale.create(new SaleId(UUID.randomUUID()), invoiceNumberExpected, LocalDateTime.now(), 100, "cash", new CustomerId(UUID.randomUUID()),saleItemsExpected, null);

        // TODO: mock soundCarrier and Product
        when(soundCarrierRepository.soundCarrierById(soundCarrierIdExpected)).thenReturn(Optional.of(soundCarrier));
        when(productRepository.productById(productIdExpected)).thenReturn(Optional.of(product));
        when(saleRepository.saleByInvoiceNumber(invoiceNumberExpected)).thenReturn(Optional.of(sale));

        // when
        Optional<SaleDTO> saleOptActual = buyingApplicationService.saleByInvoiceNumber(invoiceNumberExpected);

        // then
        assertTrue(saleOptActual.isPresent());
        SaleDTO saleActual = saleOptActual.get();
        assertEquals(sale.getInvoiceNumber(), saleActual.getInvoiceNumber());
        assertEquals(sale.getSaleItemList().size(), saleActual.getSaleItems().size());
        assertEquals(sale.getTotalPrice(), saleActual.getTotalPrice());
    }

    //not testing for unchanged amountsInStore on failure because that mechanism relies on Transactional rollback
    // and that is not available when repositories are mocked
    // there is an integration test for that scenario
}
