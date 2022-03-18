package at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import org.hibernate.id.IncrementGenerator;
import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.util.Objects;

public class SaleItem {
    private long sale_item_nr;
    private boolean isRefunded;
    private int amountOfCarriers;
    private Money pricePerCarrier;
    private SoundCarrierId carrierId;

    public static SaleItem create(boolean refunded, int anAmountOfCarriers, Money aPricePerCarrier, SoundCarrierId aCarrierId) {
        return new SaleItem(refunded, anAmountOfCarriers, aPricePerCarrier, aCarrierId);
    }
    protected SaleItem() {}
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

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleItem saleItem = (SaleItem) o;
        return isRefunded == saleItem.isRefunded && amountOfCarriers == saleItem.amountOfCarriers && Objects.equals(pricePerCarrier, saleItem.pricePerCarrier) && Objects.equals(carrierId, saleItem.carrierId);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(isRefunded, amountOfCarriers, pricePerCarrier, carrierId);
    }
}
