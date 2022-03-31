package at.fhv.ss22.ea.f.musicshop.backend.unit.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.CustomerId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.Sale;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleItem;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SaleRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HibernateSaleRepoTests {
    private SaleRepository saleRepository = InstanceProvider.getSaleRepository();

    @Test
    void given_product_when_searched_by_equal_but_not_same_id_then_product_found() {
        List<SaleItem> saleItems =  List.of(SaleItem.create(false, 1, 10, new SoundCarrierId(UUID.randomUUID())));
        Sale sale = Sale.create(new SaleId(UUID.randomUUID()), "1", LocalDateTime.now(), 100, "cash", new CustomerId(UUID.randomUUID()),saleItems, null);
        EntityManagerUtil.beginTransaction();
        saleRepository.add(sale);
        EntityManagerUtil.commit();
        SaleId surrogateId = new SaleId(sale.getSaleId().getUUID());

        //when
        Optional<Sale> saleOpt = saleRepository.saleById(surrogateId);
        //then
        assertTrue(saleOpt.isPresent());
        Sale s = saleOpt.get();
        assertEquals(sale.getSaleId(), s.getSaleId());
        assertEquals(sale.getInvoiceNumber(), s.getInvoiceNumber());
        assertEquals(sale.getPaymentMethod(), s.getPaymentMethod());
        assertEquals(sale.getTimeOfSale(), s.getTimeOfSale());
        assertEquals(sale.getPerformingEmployee(), s.getPerformingEmployee());
    }

    @Test
    void given_invalid_product_id_when_searched_then_empty_result() {
        //given
        SaleId id = new SaleId(UUID.randomUUID());

        //when
        Optional<Sale> saleOpt = saleRepository.saleById(id);

        //then
        assertTrue(saleOpt.isEmpty());
    }

    @Test
    void sale_number_tests() {
        String firstNumber = saleRepository.nextSaleNumber();
        assertEquals("R000001", firstNumber);
        EntityManagerUtil.beginTransaction();
        Sale sale = Sale.create(new SaleId(UUID.randomUUID()), firstNumber, LocalDateTime.now(), 50f, "CASH" , null, List.of(), new EmployeeId(UUID.randomUUID()));
        saleRepository.add(sale);
        EntityManagerUtil.commit();

        assertEquals("R000002", saleRepository.nextSaleNumber());
    }
}
