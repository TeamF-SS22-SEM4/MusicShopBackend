package at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.CustomerId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class Sale {
    @EmbeddedId
    private SaleId saleId;
    private String invoiceNumber;
    private LocalDateTime timeOfSale;
    private float totalPrice;
    private String paymentMethod; //maybe change to enum
    private CustomerId customerId;
    @OneToMany
    private List<SaleItem> saleItemList;
    private EmployeeId performingEmployee;

    public static Sale create(SaleId aSaleId, String aInvoiceNumber, LocalDateTime aTimeOfSale, float aTotalPrice, String aPaymentMethod, CustomerId aCustomerId, List<SaleItem> aSaleItemList, EmployeeId aPerformingEmployee) {
        return new Sale(aSaleId, aInvoiceNumber, aTimeOfSale, aTotalPrice, aPaymentMethod, aCustomerId, aSaleItemList, aPerformingEmployee);
    }

    @Generated
    protected Sale() {}

    private Sale(SaleId aSaleId, String aInvoiceNumber, LocalDateTime aTimeOfSale, float aTotalPrice, String aPaymentMethod, CustomerId aCustomerId, List<SaleItem> aSaleItemList, EmployeeId aPerformingEmployee) {
        this.saleId = aSaleId;
        this.invoiceNumber = aInvoiceNumber;
        this.timeOfSale = aTimeOfSale;
        this.totalPrice = aTotalPrice;
        this.paymentMethod = aPaymentMethod;
        this.customerId = aCustomerId;
        this.saleItemList = aSaleItemList;
        this.performingEmployee = aPerformingEmployee;
    }

    public SaleId getSaleId() {
        return saleId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public LocalDateTime getTimeOfSale() {
        return timeOfSale;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public CustomerId getCustomerOpt() {
        return customerId;
    }

    public List<SaleItem> getSaleItemList() {
        return Collections.unmodifiableList(saleItemList);
    }

    public EmployeeId getPerformingEmployee() {
        return performingEmployee;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(saleId, sale.saleId);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(saleId);
    }
}