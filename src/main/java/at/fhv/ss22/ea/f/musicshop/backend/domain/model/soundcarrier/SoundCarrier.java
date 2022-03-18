package at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import org.javamoney.moneta.Money;

import java.util.Objects;

public class SoundCarrier {
    private SoundCarrierId carrierId;
    private SoundCarrierType type;
    private Money price;
    private int amountInStore;
    private String location; //holds information in which shelve to find the carrier
    private ProductId productId;

    public static SoundCarrier create(SoundCarrierId aCarrierId, SoundCarrierType aType, Money aPrice, int aAmountInStore, String aLocation, ProductId aProductId) {
        return new SoundCarrier(aCarrierId, aType, aPrice, aAmountInStore, aLocation, aProductId);
    }

    private SoundCarrier(SoundCarrierId aCarrierId, SoundCarrierType aType, Money aPrice, int aAmountInStore, String aLocation, ProductId aProductId) {
        this.carrierId = aCarrierId;
        this.type = aType;
        this.price = aPrice;
        this.amountInStore = aAmountInStore;
        this.location = aLocation;
        this.productId = aProductId;
    }

    public SoundCarrierId getCarrierId() {
        return carrierId;
    }

    public SoundCarrierType getType() {
        return type;
    }

    public Money getPrice() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoundCarrier that = (SoundCarrier) o;
        return amountInStore == that.amountInStore && Objects.equals(carrierId, that.carrierId) && type == that.type && Objects.equals(price, that.price) && Objects.equals(location, that.location) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carrierId, type, price, amountInStore, location, productId);
    }
}
