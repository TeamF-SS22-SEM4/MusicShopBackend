package at.fhv.ss22.ea.f.musicshop.backend.unit.domain;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.CustomerId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.Sale;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleItem;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SaleTests {
    @Test
    void given_saledetails_when_creating_sale_then_details_equals() {
        // given
        UUID saleIdUUID = UUID.randomUUID();
        SaleId saleIdExpected = new SaleId(saleIdUUID);
        String invoiceNumberExpected = "20220301001";
        LocalDateTime timeOfSaleExpected = LocalDateTime.of(2022, Month.MARCH, 1, 13, 30);
        float totalPriceExpected = 50;
        String paymentMethodExpected = "Credit Card";
        CustomerId customerIdExpected = new CustomerId(UUID.randomUUID());
        List<SaleItem> saleItemsExpected = new ArrayList<>() {
            {
                add (
                    SaleItem.create(
                            1,
                            30,
                            new SoundCarrierId(UUID.randomUUID())
                    )
                );
                add (
                        SaleItem.create(
                                1,
                                20,
                                new SoundCarrierId(UUID.randomUUID())
                        )
                );
            }
        };
        UUID employeeIdUUID = UUID.randomUUID();
        EmployeeId performingEmployeeExpected = new EmployeeId(employeeIdUUID);

        // when
        Sale sale = Sale.create(
                saleIdExpected,
                invoiceNumberExpected,
                timeOfSaleExpected,
                paymentMethodExpected,
                customerIdExpected,
                saleItemsExpected,
                performingEmployeeExpected
        );

        // then
        assertEquals(saleIdExpected, sale.getSaleId());
        assertEquals(saleIdUUID, sale.getSaleId().getUUID());
        assertEquals(invoiceNumberExpected, sale.getInvoiceNumber());
        assertEquals(timeOfSaleExpected, sale.getTimeOfSale());
        assertEquals(totalPriceExpected, sale.getTotalPrice());
        assertEquals(paymentMethodExpected, sale.getPaymentMethod());
        assertEquals(customerIdExpected, sale.getCustomerOpt());
        assertEquals(saleItemsExpected.size(), sale.getSaleItemList().size());
        assertEquals(performingEmployeeExpected, sale.getPerformingEmployee());

        // Check content of lists
        for(int i = 0; i < saleItemsExpected.size(); i++) {
            SaleItem saleItemExpected = saleItemsExpected.get(i);
            SaleItem saleItemActual = sale.getSaleItemList().get(i);

            assertEquals(saleItemExpected.getAmountOfCarriers(), saleItemActual.getAmountOfCarriers());
            assertEquals(saleItemExpected.getPricePerCarrier(), saleItemActual.getPricePerCarrier());
            assertEquals(saleItemExpected.getCarrierId(), saleItemActual.getCarrierId());
            assertEquals(saleItemExpected.getCarrierId().getUUID(), saleItemActual.getCarrierId().getUUID());
        }
    }
}
