package comp3350.reshop.logic.util;

import comp3350.reshop.objects.Payment;

public class PaymentBuilder implements IPaymentBuilder {

    private Payment product;

    public PaymentBuilder() {product = new Payment();}

    @Override
    public void reset() {
        product = new Payment();
    }

    @Override
    public void setCardNumber(String number) {
        product.setCardNumber(number);
    }

    @Override
    public void setExpiry(String expiry) {
        product.setExpiry(expiry);
    }

    @Override
    public void setCvv(String cvv) {
        product.setCvv(cvv);
    }

    @Override
    public void setName(String name) {
        product.setName(name);
    }

    @Override
    public void setAddress(String address) {
        product.setAddress(address);
    }

    @Override
    public void setPostalCode(String postalCode) {
        product.setPostalCode(postalCode);
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        product.setPhoneNumber(phoneNumber);
    }

    public Payment getProduct() {
        Payment result = product;
        reset();
        return result;
    }
}
