package at.fhv.ss22.ea.f.musicshop.backend.communication.rest.objects;

import java.util.UUID;

public class OrderItem {
    private UUID carrierId;
    private int amount;

    public UUID getCarrierId() {
        return carrierId;
    }

    public int getAmount() {
        return amount;
    }
}
