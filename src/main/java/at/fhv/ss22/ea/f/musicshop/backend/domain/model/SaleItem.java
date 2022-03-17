package at.fhv.ss22.ea.f.musicshop.backend.domain.model;

import org.javamoney.moneta.Money;

public class SaleItem {
    private boolean refunded;
    private int amountOfCarriers;
    private Money pricePerCarrier;

    private SoundCarrierId carrierId;

}
