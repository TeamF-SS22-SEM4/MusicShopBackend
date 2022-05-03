package at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.CustomerId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.exceptions.CustomerAlreadyConnectedException;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Sale {
    @EmbeddedId
    private SaleId saleId;
    private String invoiceNumber;
    private LocalDateTime timeOfSale;
    private double totalPrice;
    private String paymentMethod; //maybe change to enum
    private CustomerId customerId;
    @OneToMany
    private List<SaleItem> saleItemList;
    private UserId performingEmployee;

    public static Sale newSale(String invoiceNumber, List<SaleItem> saleItems, UserId userId, String paymentMethod, CustomerId customerId) {
        Sale sale = new Sale();
        sale.saleId = new SaleId(UUID.randomUUID());
        sale.invoiceNumber = invoiceNumber;
        sale.timeOfSale = LocalDateTime.now();
        sale.paymentMethod = paymentMethod;
        sale.performingEmployee = userId;
        sale.saleItemList = saleItems;
        sale.totalPrice = saleItems.stream().mapToDouble(item -> item.getAmountOfCarriers() * item.getPricePerCarrier()).sum();
        sale.customerId = customerId;

        return sale;
    }

    public static Sale create(SaleId aSaleId, String aInvoiceNumber, LocalDateTime aTimeOfSale, String aPaymentMethod, CustomerId aCustomerId, List<SaleItem> aSaleItemList, UserId aPerformingEmployee) {
        return new Sale(aSaleId, aInvoiceNumber, aTimeOfSale, aPaymentMethod, aCustomerId, aSaleItemList, aPerformingEmployee);
    }

    @Generated
    protected Sale() {
    }

    private Sale(SaleId aSaleId, String aInvoiceNumber, LocalDateTime aTimeOfSale, String aPaymentMethod, CustomerId aCustomerId, List<SaleItem> aSaleItemList, UserId aPerformingEmployee) {
        this.saleId = aSaleId;
        this.invoiceNumber = aInvoiceNumber;
        this.timeOfSale = aTimeOfSale;
        this.paymentMethod = aPaymentMethod;
        this.customerId = aCustomerId;
        this.saleItemList = aSaleItemList;
        this.performingEmployee = aPerformingEmployee;
        this.totalPrice = saleItemList.stream().mapToDouble(item -> item.getAmountOfCarriers() * item.getPricePerCarrier()).sum();
    }

    public void addCustomer(CustomerId customerId) throws CustomerAlreadyConnectedException {
        if (null == this.customerId) {
            throw new CustomerAlreadyConnectedException();
        }
        this.customerId = customerId;
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

    public double getTotalPrice() {
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

    public UserId getPerformingEmployee() {
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
