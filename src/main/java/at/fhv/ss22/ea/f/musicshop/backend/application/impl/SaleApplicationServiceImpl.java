package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.*;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.CustomerApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RequiresRole;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.SessionKey;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.CustomerId;
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

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

@Local(SaleApplicationService.class)
@Stateless
public class SaleApplicationServiceImpl implements SaleApplicationService {

    @EJB private SoundCarrierRepository soundCarrierRepository;
    @EJB private SaleRepository saleRepository;
    @EJB private ProductRepository productRepository;
    @EJB private ArtistRepository artistRepository;
    @EJB private SessionRepository sessionRepository;

    @EJB private CustomerApplicationService customerApplicationService;

    public SaleApplicationServiceImpl() {}

    public SaleApplicationServiceImpl(SessionRepository sessionRepository, SoundCarrierRepository soundCarrierRepository, SaleRepository saleRepository,
                                      ProductRepository productRepository, ArtistRepository artistRepository) {
        this.sessionRepository = sessionRepository;
        this.soundCarrierRepository = soundCarrierRepository;
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.artistRepository = artistRepository;
    }


    @Override
    @RequiresRole(UserRole.CUSTOMER)
    public String buyAsCustomer(@SessionKey String sessionId, List<SoundCarrierAmountDTO> soundCarriers,
                                String paymentMethod, String creditCardType, String creditCardNumber, String cvc) throws SessionExpired, NoPermissionForOperation, RemoteException, CarrierNotAvailableException, UnsupportedOperationException {
        Session session = sessionRepository.sessionById(new SessionId(sessionId)).orElseThrow(IllegalStateException::new);
        UUID customerId = session.getUserId().getUUID();
        if(paymentMethod.equals("Credit Card")) {
            // TODO: Add credit card information to DTO
            CustomerDTO customer = customerApplicationService.customerById(sessionId, customerId);

            if(!customer.getCreditCardType().equals(creditCardType) ||
                    !customer.getCreditCardNumber().equals(creditCardNumber) ||
                    !customer.getCvc().equals(cvc)) {
                // Which Exceptiontype?
                throw new UnsupportedOperationException("Credit card information invalid");
            }
        }

        return buy(sessionId, soundCarriers, paymentMethod, customerId);
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public String buy(@SessionKey String sessionId, List<SoundCarrierAmountDTO> carrierAmounts, String paymentMethod, UUID customerId) throws CarrierNotAvailableException {
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

        // Maybe check Sessionid earlier before adding saleItems
        Session session = sessionRepository.sessionById(new SessionId(sessionId)).orElseThrow(IllegalStateException::new);
        long currentAmountOfSales = saleRepository.amountOfSales();
        Sale sale = Sale.newSale("R" + String.format("%06d", currentAmountOfSales + 1), saleItems, session.getUserId(), paymentMethod, new CustomerId(customerId));
        saleRepository.add(sale);
        EntityManagerUtil.commit();

        return sale.getInvoiceNumber();
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public SaleDTO saleByInvoiceNumber(@SessionKey String sessionId, String invoiceNumber) {
        return saleRepository.saleByInvoiceNumber(invoiceNumber).map(this::saleDtoFromSale).orElseThrow(NoSuchElementException::new);
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public void refund(@SessionKey String sessionId, String invoiceNumber, List<RefundedSaleItemDTO> refundedSaleItems) {
        EntityManagerUtil.beginTransaction();

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
        List<SaleItemDTO> saleItemDTOs = new ArrayList<>();

        for (SaleItem saleItem : sale.getSaleItemList()) {
            SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierById(saleItem.getCarrierId()).orElseThrow(IllegalStateException::new);

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
