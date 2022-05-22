package at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class DigitalProductPurchased {
    @EmbeddedId
    private DigitalProductPurchasedId eventId;
    private String username;
    private ProductId productId;

    public DigitalProductPurchased(DigitalProductPurchasedId eventId, String username, ProductId productId) {
        this.eventId = eventId;
        this.username = username;
        this.productId = productId;

    }

    protected DigitalProductPurchased() {

    }

    public DigitalProductPurchasedId getEventId() {
        return eventId;
    }

    public String getUsername() {
        return username;
    }

    public ProductId getProductId() {
        return productId;
    }
}
