package at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class SaleItem {
    @Id
    @GeneratedValue
    private long hibernateId;

    private int amountOfCarriers;
    private float pricePerCarrier;
    private int refundedAmount;
    private SoundCarrierId carrierId;

    public static SaleItem create(int anAmountOfCarriers, float aPricePerCarrier, SoundCarrierId aCarrierId) {
        return new SaleItem(anAmountOfCarriers, aPricePerCarrier, aCarrierId);
    }
    public static SaleItem ofCarrier(int amount, SoundCarrier carrier) {
        return new SaleItem(amount, carrier.getPrice(), carrier.getCarrierId());
    }

    @Generated
    protected SaleItem() {
    }

    private SaleItem(int anAmountOfCarriers, float aPricePerCarrier, SoundCarrierId aCarrierId) {
        this.amountOfCarriers = anAmountOfCarriers;
        this.pricePerCarrier = aPricePerCarrier;
        this.refundedAmount = 0;
        this.carrierId = aCarrierId;
    }

    public void refund(int amount) {
       if(refundedAmount <= amountOfCarriers) {
           refundedAmount += amount;
       } else {
           // TODO: Use appropriate exception
           throw new UnsupportedOperationException("You can't refund more than you bought.");
       }
    }

    public int getAmountOfCarriers() {
        return amountOfCarriers;
    }

    public float getPricePerCarrier() {
        return pricePerCarrier;
    }

    public int getRefundedAmount() {
        return refundedAmount;
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
        return hibernateId == saleItem.hibernateId && amountOfCarriers == saleItem.amountOfCarriers && Float.compare(saleItem.pricePerCarrier, pricePerCarrier) == 0 && refundedAmount == saleItem.refundedAmount && Objects.equals(carrierId, saleItem.carrierId);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(hibernateId, amountOfCarriers, pricePerCarrier, refundedAmount, carrierId);
    }
}
