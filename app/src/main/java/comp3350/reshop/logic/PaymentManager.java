package comp3350.reshop.logic;

import java.util.Calendar;

import comp3350.reshop.data.ClothingItemData;
import comp3350.reshop.logic.exceptions.InvalidInputException;
import comp3350.reshop.logic.exceptions.InvalidItemException;
import comp3350.reshop.logic.validation.PaymentValidator;
import comp3350.reshop.objects.ClothingItem;
import comp3350.reshop.objects.Payment;

public class PaymentManager {
    private final ClothingItemAccessor accessor;

    public PaymentManager() {
        accessor = new ClothingItemAccessor();
    }

    public PaymentManager(ClothingItemData clothingItemData) {
        accessor = new ClothingItemAccessor(clothingItemData);
    }

    private void validatePayment(Payment payment) throws InvalidInputException {
        int currentYearInt = (Calendar.getInstance().get(Calendar.YEAR)) % 100; // last 2 digits of current year
        int currentMonthInt = Calendar.getInstance().get(Calendar.MONTH) + 1;   // month starts at 0

        PaymentValidator.validateCardNumber(payment.getCardNumber());
        PaymentValidator.validateCardExpiryDate(payment.getExpiry(), currentMonthInt, currentYearInt);
        PaymentValidator.validateCVV(payment.getCvv());
        PaymentValidator.validateCardHolderName(payment.getName());
        PaymentValidator.validateCardHolderAddress(payment.getAddress());
        PaymentValidator.validateCardHolderPostalCode(payment.getPostalCode());
        PaymentValidator.validatePhoneNumber(payment.getPhoneNumber());
    }

    public void processPayment(Payment payment, int itemID, String currentUser) throws InvalidInputException {
        validatePayment(payment);

        if (itemID <= 0) {
            throw new InvalidItemException("The chosen item appears to be invalid.");
        }

        ClothingItem item = accessor.getItemById(itemID);

        if (item == null) {
            throw new InvalidItemException("The chosen item doesn't exist.");
        }

        if (item.getBuyer() == null) {
            accessor.setBuyer(currentUser, itemID);
        } else {
            throw new InvalidItemException("The chosen item has already been sold.");
        }
    }
}
