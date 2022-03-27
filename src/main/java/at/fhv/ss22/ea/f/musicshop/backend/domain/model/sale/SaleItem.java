package at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class SaleItem {
    @Id
    @GeneratedValue
    private long hibernateId;

    private boolean isRefunded;
    private int amountOfCarriers;
    private float pricePerCarrier;
    private SoundCarrierId carrierId;

    public static SaleItem create(boolean refunded, int anAmountOfCarriers, float aPricePerCarrier, SoundCarrierId aCarrierId) {
        return new SaleItem(refunded, anAmountOfCarriers, aPricePerCarrier, aCarrierId);
    }
    public static SaleItem ofCarrier(int amount, SoundCarrier carrier) {
        return new SaleItem(false, amount, carrier.getPrice(), carrier.getCarrierId());
    }

    @Generated
    protected SaleItem() {}

    private SaleItem(boolean refunded, int anAmountOfCarriers, float aPricePerCarrier, SoundCarrierId aCarrierId) {
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

    public float getPricePerCarrier() {
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
