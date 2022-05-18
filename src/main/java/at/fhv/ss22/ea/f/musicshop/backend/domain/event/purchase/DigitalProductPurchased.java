package at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class DigitalProductPurchased {
    @EmbeddedId
    private DigitalProductPurchasedId eventId;
    private UserId userId;
    private String username;
    private ProductId productId;
    private String productName;
    private String duration;
    private SoundCarrierId carrierId;

    public DigitalProductPurchased(DigitalProductPurchasedId eventId, UserId userId, String username, ProductId productId, String productName, String duration, SoundCarrierId carrierId) {
        this.eventId = eventId;
        this.userId = userId;
        this.username = username;
        this.productId = productId;
        this.productName = productName;
        this.duration = duration;
        this.carrierId = carrierId;
    }

    protected DigitalProductPurchased() {

    }

    public DigitalProductPurchasedId getEventId() {
        return eventId;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public ProductId getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getDuration() {
        return duration;
    }

    public SoundCarrierId getCarrierId() {
        return carrierId;
    }
}
