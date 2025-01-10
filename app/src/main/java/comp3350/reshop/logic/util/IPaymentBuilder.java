package comp3350.reshop.logic.util;

public interface IPaymentBuilder {

    void reset();
    void setCardNumber(String number);
    void setExpiry(String expiry);
    void setCvv(String cvv);
    void setName(String name);
    void setAddress(String address);
    void setPostalCode(String postalCode);
    void setPhoneNumber(String phoneNumber);
}
