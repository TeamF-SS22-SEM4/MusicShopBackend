package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.communication.dto.RefundedSaleItemDTO;
import at.fhv.ss22.ea.f.communication.dto.SaleDTO;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RequiresRole;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.SessionKey;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SaleApplicationService {

    @RequiresRole(role = UserRole.EMPLOYEE)
    String buy(@SessionKey String sessionId, List<SoundCarrierAmountDTO> soundCarriers, String paymentMethod, UUID customerId) throws CarrierNotAvailableException, SessionExpired, NoPermissionForOperation;

    @RequiresRole(role = UserRole.EMPLOYEE)
    Optional<SaleDTO> saleByInvoiceNumber(@SessionKey String sessionId, String invoiceNumber) throws SessionExpired, NoPermissionForOperation;

    @RequiresRole(role = UserRole.EMPLOYEE)
    void refund(@SessionKey String sessionId, String invoiceNumber, List<RefundedSaleItemDTO> refundedSaleItems) throws SessionExpired, NoPermissionForOperation;
}
