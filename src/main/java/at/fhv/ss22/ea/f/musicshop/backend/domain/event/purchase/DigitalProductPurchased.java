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
    private ProductId productId;

    public DigitalProductPurchased(DigitalProductPurchasedId eventId, UserId userId, ProductId productId) {
        this.eventId = eventId;
        this.userId = userId;
        this.productId = productId;

    }

    protected DigitalProductPurchased() {

    }

    public DigitalProductPurchasedId getEventId() {
        return eventId;
    }

    public UserId getUserId() {
        return userId;
    }

    public ProductId getProductId() {
        return productId;
    }
}
