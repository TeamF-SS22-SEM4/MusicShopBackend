package at.fhv.ss22.ea.f.musicshop.backend.domain.model;

import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.util.Currency;

public class SoundCarrier {
    private SoundCarrierId carrierId;
    private SoundCarrierType type;
    private Money price;
    private int amountInStore;
    private String location; //holds information in which shelve to find the carrier

    private ProductId productId;

}
