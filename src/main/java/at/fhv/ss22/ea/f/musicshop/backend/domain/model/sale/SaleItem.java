package at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import org.javamoney.moneta.Money;

import java.util.Objects;

public class SaleItem {
    private boolean isRefunded;
    private int amountOfCarriers;
    private Money pricePerCarrier;
    private SoundCarrierId carrierId;

    public static SaleItem create(boolean refunded, int anAmountOfCarriers, Money aPricePerCarrier, SoundCarrierId aCarrierId) {
        return new SaleItem(refunded, anAmountOfCarriers, aPricePerCarrier, aCarrierId);
    }

    private SaleItem(boolean refunded, int anAmountOfCarriers, Money aPricePerCarrier, SoundCarrierId aCarrierId) {
        this.isRefunded = refunded;
        this.amountOfCarriers = anAmountOfCarriers;
        this.pricePerCarrier = aPricePerCarrier;
        this.carrierId = aCarrierId;
    }

    public boolean isRefunded() {
        return isRefunded;
    }

    public int getAmountOfCarriers() {
        return amountOfCarriers;
    }

    public Money getPricePerCarrier() {
        return pricePerCarrier;
    }

    public SoundCarrierId getCarrierId() {
        return carrierId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleItem saleItem = (SaleItem) o;
        return isRefunded == saleItem.isRefunded && amountOfCarriers == saleItem.amountOfCarriers && Objects.equals(pricePerCarrier, saleItem.pricePerCarrier) && Objects.equals(carrierId, saleItem.carrierId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isRefunded, amountOfCarriers, pricePerCarrier, carrierId);
    }
}
