package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class SoundCarrierOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    //TODO move to shared lib

    private UUID orderId;

    private UUID soundCarrierId;

    private int amount;

    private SoundCarrierOrderDTO() {}

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getSoundCarrierId() {
        return soundCarrierId;
    }

    public int getAmount() {
        return amount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final SoundCarrierOrderDTO instance;

        public Builder() {
            this.instance = new SoundCarrierOrderDTO();
        }

        public Builder withOrderId(UUID uuid) {
            this.instance.orderId = uuid;
            return this;
        }
        public Builder withCarrierId(UUID carrierId) {
            this.instance.soundCarrierId = carrierId;
            return this;
        }
        public Builder withAmount(int amount) {
            this.instance.amount = amount;
            return this;
        }
        public SoundCarrierOrderDTO build() {
            Objects.requireNonNull(this.instance.orderId);
            return this.instance;
        }
    }
}
