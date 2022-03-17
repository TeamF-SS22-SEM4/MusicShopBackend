package at.fhv.ss22.ea.f.musicshop.backend.domain.model;

import org.javamoney.moneta.Money;

import javax.management.loading.PrivateMLet;
import java.time.LocalDateTime;
import java.util.List;
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
}
