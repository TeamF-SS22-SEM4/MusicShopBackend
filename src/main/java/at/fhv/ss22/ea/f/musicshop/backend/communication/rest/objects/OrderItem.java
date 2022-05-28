package at.fhv.ss22.ea.f.musicshop.backend.communication.rest.objects;

import java.util.UUID;

public class OrderItem {
    private UUID carrierId;
    private int amount;

    public OrderItem() {
    }

    public OrderItem(UUID carrierId, int amount) {
        this.carrierId = carrierId;
        this.amount = amount;
    }

    public UUID getCarrierId() {
        return carrierId;
    }

    public int getAmount() {
        return amount;
    }
}
