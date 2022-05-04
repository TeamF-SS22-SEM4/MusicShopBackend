package at.fhv.ss22.ea.f.musicshop.backend.communication.rest.objects;

public class PaymentInformation {
    private String paymentMethod;
    private String creditCardType;
    private String creditCardNumber;
    private String cvc;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getCvc() {
        return cvc;
    }
}
