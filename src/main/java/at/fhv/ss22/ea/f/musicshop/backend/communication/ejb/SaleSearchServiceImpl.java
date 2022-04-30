package at.fhv.ss22.ea.f.musicshop.backend.communication.ejb;

import at.fhv.ss22.ea.f.communication.api.SaleSearchService;
import at.fhv.ss22.ea.f.communication.dto.SaleDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.NoSuchElementException;

@Remote(SaleSearchService.class)
@Stateless
public class SaleSearchServiceImpl implements SaleSearchService {

    @EJB
    private SaleApplicationService saleApplicationService;

    @Override
    public SaleDTO saleByInvoiceNumber(String sessionId, String invoiceNumber) throws SessionExpired, NoPermissionForOperation {
       return saleApplicationService.saleByInvoiceNumber(sessionId, invoiceNumber).orElseThrow(NoSuchElementException::new);
    }
}
