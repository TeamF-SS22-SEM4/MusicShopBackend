package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.BuyingApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.exceptions.SoundCarrierUnavailableException;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.Sale;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleItem;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SaleRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class BuyingApplicationServiceImpl implements BuyingApplicationService {

    private SoundCarrierRepository soundCarrierRepository;

    private SaleRepository saleRepository;

    public BuyingApplicationServiceImpl(SoundCarrierRepository soundCarrierRepository, SaleRepository saleRepository) {
        this.soundCarrierRepository = soundCarrierRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public UUID buy(List<SoundCarrierAmountDTO> carrierAmounts, String paymentMethod) throws CarrierNotAvailableException {
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

        //TODO add employee
        Sale sale = Sale.newSale(saleRepository.nextSaleNumber(), saleItems, new EmployeeId(UUID.randomUUID()), paymentMethod);
        saleRepository.add(sale);

        EntityManagerUtil.commit();

        return sale.getSaleId().getUUID();
    }
}
