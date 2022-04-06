package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.RefundedSaleItemDTO;
import at.fhv.ss22.ea.f.communication.dto.SaleDTO;
import at.fhv.ss22.ea.f.communication.dto.SaleItemDTO;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.exceptions.SoundCarrierUnavailableException;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.Sale;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleItem;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.*;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;

import java.util.*;
import java.util.stream.Collectors;

public class SaleApplicationServiceImpl implements SaleApplicationService {

    private SoundCarrierRepository soundCarrierRepository;
    private SaleRepository saleRepository;
    private ProductRepository productRepository;
    private ArtistRepository artistRepository;
    private AuthenticationApplicationService authenticationApplicationService;
    private SessionRepository sessionRepository;

    public SaleApplicationServiceImpl(SessionRepository sessionRepository, AuthenticationApplicationService authenticationApplicationService, SoundCarrierRepository soundCarrierRepository, SaleRepository saleRepository,
                                      ProductRepository productRepository, ArtistRepository artistRepository) {
        this.sessionRepository = sessionRepository;
        this.authenticationApplicationService = authenticationApplicationService;
        this.soundCarrierRepository = soundCarrierRepository;
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    public String buy(String sessionId, List<SoundCarrierAmountDTO> carrierAmounts, String paymentMethod) throws CarrierNotAvailableException, SessionExpired, NoPermissionForOperation {
        if (!authenticationApplicationService.hasRole(new SessionId(sessionId), UserRole.EMPLOYEE)) {
            throw new NoPermissionForOperation();
        }
        EntityManagerUtil.beginTransaction();
        List<SaleItem> saleItems = new LinkedList<>();
        List<UUID> invalidCarriers = new LinkedList<>();
        for (SoundCarrierAmountDTO dto : carrierAmounts) {
            SoundCarrier carrier = soundCarrierRepository.soundCarrierById(new SoundCarrierId(dto.getCarrierId())).orElseThrow(IllegalStateException::new);

            try {
                saleItems.add(carrier.sell(dto.getAmount()));
            } catch (SoundCarrierUnavailableException e) {
                invalidCarriers.add(carrier.getCarrierId().getUUID());
            }
        }

        if (!invalidCarriers.isEmpty()) {
            EntityManagerUtil.rollback();
            throw new CarrierNotAvailableException(invalidCarriers);
        }

        Session session = sessionRepository.sessionById(new SessionId(sessionId)).orElseThrow(IllegalStateException::new);
        long currentAmountOfSales = saleRepository.amountOfSales();
        Sale sale = Sale.newSale("R" + String.format("%06d", currentAmountOfSales + 1), saleItems, session.getEmployeeId(), paymentMethod);
        saleRepository.add(sale);
        EntityManagerUtil.commit();

        return sale.getInvoiceNumber();
    }

    @Override
    public Optional<SaleDTO> saleByInvoiceNumber(String sessionId, String invoiceNumber) throws SessionExpired, NoPermissionForOperation {
        if (!authenticationApplicationService.hasRole(new SessionId(sessionId), UserRole.EMPLOYEE)) {
            throw new NoPermissionForOperation();
        }
        return saleRepository.saleByInvoiceNumber(invoiceNumber).map(this::saleDtoFromSale);
    }

    @Override
    public void refund(String sessionId, String invoiceNumber, List<RefundedSaleItemDTO> refundedSaleItems) throws SessionExpired, NoPermissionForOperation {
        if (!authenticationApplicationService.hasRole(new SessionId(sessionId), UserRole.EMPLOYEE)) {
            throw new NoPermissionForOperation();
        }
        EntityManagerUtil.beginTransaction();
        //TODO replace with domain specific exceptions
        Sale sale = saleRepository.saleByInvoiceNumber(invoiceNumber).orElseThrow(NoSuchElementException::new);

        refundedSaleItems.forEach(refundedSaleItem -> {
            // Set matching saleItem isRefund to true
            SaleItem saleItem = sale.getSaleItemList().stream().filter(si ->
                    si.getCarrierId().getUUID().equals(refundedSaleItem.getSoundCarrierId())
            ).findFirst().orElseThrow(NoSuchElementException::new);

            saleItem.refund(refundedSaleItem.getAmountToRefund());

            SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierById(saleItem.getCarrierId()).orElseThrow(NoSuchElementException::new);
            soundCarrier.refund(refundedSaleItem.getAmountToRefund());
        });

        EntityManagerUtil.commit();
    }

    private SaleDTO saleDtoFromSale(Sale sale) {
        // TODO: Get carrier and product infos with stream, map and check if isPresent
        List<SaleItemDTO> saleItemDTOs = new ArrayList<>();

        for (SaleItem saleItem : sale.getSaleItemList()) {
            SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierById(saleItem.getCarrierId()).orElseThrow(IllegalStateException::new);
            //TODO replace with domain specific exceptions
            Product product = productRepository.productById(soundCarrier.getProductId()).orElseThrow(IllegalStateException::new);

            SaleItemDTO dto = SaleItemDTO.builder()
                    .withProductName(product.getName())
                    .withArtistName(product.getArtistIds().stream()
                            .map(artistId -> artistRepository.artistById(artistId))
                            .filter(Optional::isPresent)
                            .map(opt -> opt.get().getArtistName())
                            .collect(Collectors.joining(", ")))
                    .withSoundCarrierId(soundCarrier.getCarrierId().getUUID())
                    .withSoundCarrierName(soundCarrier.getType().getFriendlyName())
                    .withAmountOfCarriers(saleItem.getAmountOfCarriers())
                    .withPricePerCarrier(saleItem.getPricePerCarrier())
                    .withRefundedAmount(saleItem.getRefundedAmount())
                    .build();

            saleItemDTOs.add(dto);
        }

        return SaleDTO.builder()
                .withInvoiceNumber(sale.getInvoiceNumber())
                .withSaleItems(saleItemDTOs)
                .withTotalPrice(sale.getTotalPrice())
                .build();
    }
}
