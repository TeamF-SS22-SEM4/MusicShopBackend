package at.fhv.ss22.ea.f.musicshop.backend.communication.rest.objects;

public class Purchase {
    private OrderItem[] orderItems;
    private PaymentInformation paymentInformation;

    public OrderItem[] getOrderItems() {
        return orderItems;
    }

    public PaymentInformation getPaymentInformation() {
        return paymentInformation;
    }
}
