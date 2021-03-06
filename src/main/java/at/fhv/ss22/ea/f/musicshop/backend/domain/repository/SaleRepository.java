package at.fhv.ss22.ea.f.musicshop.backend.domain.repository;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.CustomerId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.Sale;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;

import java.util.List;
import java.util.Optional;

public interface SaleRepository {

    void add(Sale sale);

    Optional<Sale> saleById(SaleId saleId);

    Optional<Sale> saleByInvoiceNumber(String invoiceNumber);

    long amountOfSales();

    public List<Sale> salesByCustomerId(CustomerId customerId);
}
