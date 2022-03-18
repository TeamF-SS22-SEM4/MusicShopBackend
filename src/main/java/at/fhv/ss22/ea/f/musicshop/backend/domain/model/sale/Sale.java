package at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.CustomerId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;
import org.javamoney.moneta.Money;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Sale {
    private SaleId saleId;
    private String invoiceNumber;
    private LocalDateTime timeOfSale;
    private Money totalPrice;
    private String paymentMethod; //maybe change to enum
    private Optional<CustomerId> customerOpt;
    private List<SaleItem> saleItemList;
    private EmployeeId performingEmployee;

    public static Sale create(SaleId aSaleId, String aInvoiceNumber, LocalDateTime aTimeOfSale, Money aTotalPrice, String aPaymentMethod, Optional<CustomerId> aCustomerOpt, List<SaleItem> aSaleItemList, EmployeeId aPerformingEmployee) {
        return new Sale(aSaleId, aInvoiceNumber, aTimeOfSale, aTotalPrice, aPaymentMethod, aCustomerOpt, aSaleItemList, aPerformingEmployee);
    }

    private Sale(SaleId aSaleId, String aInvoiceNumber, LocalDateTime aTimeOfSale, Money aTotalPrice, String aPaymentMethod, Optional<CustomerId> aCustomerOpt, List<SaleItem> aSaleItemList, EmployeeId aPerformingEmployee) {
        this.saleId = aSaleId;
        this.invoiceNumber = aInvoiceNumber;
        this.timeOfSale = aTimeOfSale;
        this.totalPrice = aTotalPrice;
        this.paymentMethod = aPaymentMethod;
        this.customerOpt = aCustomerOpt;
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

    public Money getTotalPrice() {
        return totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Optional<CustomerId> getCustomerOpt() {
        return customerOpt;
    }

    public List<SaleItem> getSaleItemList() {
        return saleItemList;
    }

    public EmployeeId getPerformingEmployee() {
        return performingEmployee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(saleId, sale.saleId) && Objects.equals(invoiceNumber, sale.invoiceNumber) && Objects.equals(timeOfSale, sale.timeOfSale) && Objects.equals(totalPrice, sale.totalPrice) && Objects.equals(paymentMethod, sale.paymentMethod) && Objects.equals(customerOpt, sale.customerOpt) && Objects.equals(saleItemList, sale.saleItemList) && Objects.equals(performingEmployee, sale.performingEmployee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(saleId, invoiceNumber, timeOfSale, totalPrice, paymentMethod, customerOpt, saleItemList, performingEmployee);
    }
}
