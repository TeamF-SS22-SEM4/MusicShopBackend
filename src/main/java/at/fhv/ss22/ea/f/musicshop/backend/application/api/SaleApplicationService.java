package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.communication.dto.RefundedSaleItemDTO;
import at.fhv.ss22.ea.f.communication.dto.SaleDTO;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;

import javax.ejb.Local;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Local
public interface SaleApplicationService {

    String buy(String sessionId, List<SoundCarrierAmountDTO> soundCarriers, String paymentMethod, UUID customerId) throws CarrierNotAvailableException, SessionExpired, NoPermissionForOperation;

    Optional<SaleDTO> saleByInvoiceNumber(String sessionId, String invoiceNumber) throws SessionExpired, NoPermissionForOperation;

    void refund(String sessionId, String invoiceNumber, List<RefundedSaleItemDTO> refundedSaleItems) throws SessionExpired, NoPermissionForOperation;
}
