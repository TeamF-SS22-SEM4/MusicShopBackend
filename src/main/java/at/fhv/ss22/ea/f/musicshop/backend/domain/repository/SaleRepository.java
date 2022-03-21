package at.fhv.ss22.ea.f.musicshop.backend.domain.repository;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.Sale;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;

import java.util.Optional;

public interface SaleRepository {

    void add(Sale sale);

    Optional<Sale> saleById(SaleId saleId);
}
