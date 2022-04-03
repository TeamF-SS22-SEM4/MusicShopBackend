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

        EntityManagerUtil.rollback();
    }

    @Test
    void given_invoiceNumber_when_search_byInvoiceNumber_then_return_matchingSale() {
        // given
        String invoiceNumberExpected = "42";
        List<SaleItem> saleItemsExpected =  List.of(SaleItem.create(false, 1, 10, new SoundCarrierId(UUID.randomUUID())));
        Sale sale = Sale.create(new SaleId(UUID.randomUUID()), invoiceNumberExpected, LocalDateTime.now(), 100, "cash", new CustomerId(UUID.randomUUID()),saleItemsExpected, null);
        EntityManagerUtil.beginTransaction();
        saleRepository.add(sale);

        // when
        Optional<Sale> saleOptActual = saleRepository.saleByInvoiceNumber(invoiceNumberExpected);

        // then
        assertTrue(saleOptActual.isPresent());
        Sale saleActual = saleOptActual.get();
        assertEquals(sale.getSaleId(), saleActual.getSaleId());
        assertEquals(sale.getInvoiceNumber(), saleActual.getInvoiceNumber());
        assertEquals(sale.getPaymentMethod(), saleActual.getPaymentMethod());
        assertEquals(sale.getTimeOfSale(), saleActual.getTimeOfSale());
        assertEquals(sale.getPerformingEmployee(), saleActual.getPerformingEmployee());

        EntityManagerUtil.rollback();
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
    void given_invalid_invoiceNumber_when_searchByInvoiceNumber_then_empty_result() {
        // given
        String invoiceNumber = "42";

        //when
        Optional<Sale> saleOpt = saleRepository.saleByInvoiceNumber(invoiceNumber);

        // then
        assertTrue(saleOpt.isEmpty());
    }

    @Test
    void given_3sales_in_repository_when_getAmountOfSales_3isReturned() {
        // given
        long amountOfSalesExpected = 3;
        List<Sale> sales = List.of(
                Sale.create(
                        new SaleId(UUID.randomUUID()),
                        "R000001",
                        LocalDateTime.now(),
                        50f,
                        "CASH" , null,
                        List.of(),
                        new EmployeeId(UUID.randomUUID())
                ),
                Sale.create(
                        new SaleId(UUID.randomUUID()),
                        "R000002",
                        LocalDateTime.now(),
                        50f,
                        "CASH" , null,
                        List.of(),
                        new EmployeeId(UUID.randomUUID())
                ),
                Sale.create(
                        new SaleId(UUID.randomUUID()),
                        "R000003",
                        LocalDateTime.now(),
                        50f,
                        "CASH" , null,
                        List.of(),
                        new EmployeeId(UUID.randomUUID())
                )
        );

        EntityManagerUtil.beginTransaction();
        sales.forEach(sale -> saleRepository.add(sale));

        // when
        long amountOfSalesActual = saleRepository.amountOfSales();

        // then
        assertEquals(amountOfSalesExpected, amountOfSalesActual);

        EntityManagerUtil.rollback();
    }
}
