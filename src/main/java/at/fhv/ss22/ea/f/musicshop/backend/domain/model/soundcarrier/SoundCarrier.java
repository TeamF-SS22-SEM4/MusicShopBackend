package at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier;

import at.fhv.ss22.ea.f.musicshop.backend.application.api.SoundCarrierOrderDTO;
import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.exceptions.SoundCarrierUnavailableException;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleItem;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class SoundCarrier {
    @EmbeddedId
    @AttributeOverride(name = "carrierId", column = @Column(name = "carrierId"))
    private SoundCarrierId carrierId;
    private SoundCarrierType type;
    private float price;
    private int amountInStore;
    private String location; //holds information in which shelve to find the carrier
    @AttributeOverride(name = "productId", column = @Column(name = "productId"))
    private ProductId productId;

    public static SoundCarrier create(SoundCarrierId aCarrierId, SoundCarrierType aType, float aPrice, int aAmountInStore, String aLocation, ProductId aProductId) {
        return new SoundCarrier(aCarrierId, aType, aPrice, aAmountInStore, aLocation, aProductId);
    }

    @Generated
    protected SoundCarrier() {
    }

    private SoundCarrier(SoundCarrierId aCarrierId, SoundCarrierType aType, float aPrice, int aAmountInStore, String aLocation, ProductId aProductId) {
        this.carrierId = aCarrierId;
        this.type = aType;
        this.price = aPrice;
        this.amountInStore = aAmountInStore;
        this.location = aLocation;
        this.productId = aProductId;
    }

    public SaleItem sell(int amount) throws SoundCarrierUnavailableException {
        if (amount > this.amountInStore) {
            throw new SoundCarrierUnavailableException();
        }
        this.amountInStore -= amount;

        return SaleItem.ofCarrier(amount, this);
    }

    public void refund(int amount) {
        if(amount > 0) {
            this.amountInStore += amount;
        }
        // TODO: else throw appropriate exception
    }

    public void approvedOrder(SoundCarrierOrderDTO orderDTO) {
        if (orderDTO.getAmount() < 0 ) {
            throw new IllegalStateException("Amount must not be negative");
        }
        this.amountInStore += orderDTO.getAmount();
    }

    public SoundCarrierId getCarrierId() {
        return carrierId;
    }

    public SoundCarrierType getType() {
        return type;
    }

    public float getPrice() {
        return price;
    }

    public int getAmountInStore() {
        return amountInStore;
    }

    public String getLocation() {
        return location;
    }

    public ProductId getProductId() {
        return productId;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoundCarrier that = (SoundCarrier) o;
        return Objects.equals(carrierId, that.carrierId);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(carrierId);
    }
}
