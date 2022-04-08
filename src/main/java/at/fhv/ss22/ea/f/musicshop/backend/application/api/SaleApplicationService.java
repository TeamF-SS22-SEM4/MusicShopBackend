package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import at.fhv.ss22.ea.f.communication.dto.RefundedSaleItemDTO;
import at.fhv.ss22.ea.f.communication.dto.SaleDTO;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;

public interface SaleApplicationService {
    //TODO add employeeId once client has logged in employee
    String buy(List<SoundCarrierAmountDTO> soundCarriers, String paymentMethod, UUID customerId) throws CarrierNotAvailableException;

    Optional<SaleDTO> saleByInvoiceNumber(String invoiceNumber);

    void refund(String invoiceNumber, List<RefundedSaleItemDTO> refundedSaleItems);
}
