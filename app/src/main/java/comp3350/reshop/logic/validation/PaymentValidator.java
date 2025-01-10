package comp3350.reshop.logic.validation;

import comp3350.reshop.logic.exceptions.EmptyInputException;
import comp3350.reshop.logic.exceptions.ExpiredCardException;
import comp3350.reshop.logic.exceptions.InvalidInputException;
import comp3350.reshop.logic.exceptions.InvalidInputFormatException;
import comp3350.reshop.logic.exceptions.UnsupportedCardTypeException;

public final class PaymentValidator {
    private PaymentValidator() {
        // prevents instantiation
    }

    public static void validateCardNumber(String cardNumber) throws InvalidInputException {
        if (cardNumber.isEmpty() || cardNumber.trim().isEmpty()) {
            throw new EmptyInputException("Card number must not be empty.");
        }

        if (!cardNumber.trim().matches("^[0-9][0-9][0-9][0-9] [0-9][0-9][0-9][0-9] [0-9][0-9][0-9][0-9] [0-9][0-9][0-9][0-9]$")) {
            throw new InvalidInputFormatException("Card number must have the format (#### #### #### ####).");
        }

        char cardTypeIndicator = cardNumber.charAt(0);

        if (cardTypeIndicator != '4' && cardTypeIndicator != '5') {
            throw new UnsupportedCardTypeException("Card number must either be a Mastercard (starts with 5) or Visa (starts with 4).");
        }
    }

    public static void validateCardExpiryDate(String expiryDate, int currentMonth, int currentYear) throws InvalidInputException {
        if (expiryDate.isEmpty() || expiryDate.trim().isEmpty()) {
            throw new EmptyInputException("Expiry date must not be empty.");
        }

        if (!expiryDate.trim().matches("^[0-9][0-9]/[0-9][0-9]$")) {
            throw new InvalidInputFormatException("Expiry date must have the format MM/YY where MM and YY are both numbers.");
        }

        String expiryDateYear = expiryDate.trim().substring(3,5);
        String expiryDateMonth = expiryDate.trim().substring(0,2);

        int expiryYearInt = Integer.parseInt(expiryDateYear);
        int expiryMonthInt = Integer.parseInt(expiryDateMonth);

        if (expiryMonthInt < 1 || expiryMonthInt > 12) {
            throw new InvalidInputFormatException("Expiry date's month must be within 01 and 12.");
        }

        if (expiryYearInt < currentYear || (expiryMonthInt < currentMonth && expiryYearInt == currentYear)) {
            throw new ExpiredCardException("Expiry date must not have already passed.");
        }
    }

    public static void validateCVV(String CVV) throws InvalidInputException {
        if (CVV.isEmpty() || CVV.trim().isEmpty()) {
            throw new EmptyInputException("CVV must not be empty.");
        }

        if (CVV.length() > 3) {
            throw new InvalidInputFormatException("CVV must not be more than 3 numbers");
        }

        if (!CVV.matches("[0-9]+")) {
            throw new InvalidInputFormatException("CVV must contain only numbers.");
        }
    }

    public static void validatePhoneNumber(String phoneNumber) throws InvalidInputException {
        if (phoneNumber.isEmpty() || phoneNumber.trim().isEmpty()) {
            throw new EmptyInputException("Phone number must not be empty.");
        }

        if (phoneNumber.length() > 10) {
            throw new InvalidInputFormatException("Phone number must not be more than 10 digits");
        }

        if (!phoneNumber.matches("[0-9]+")) {
            throw new InvalidInputFormatException("Phone number must contain only numbers.");
        }
    }

    public static void validateCardHolderName(String name) throws InvalidInputException {
        if (name.isEmpty() || name.trim().isEmpty()) {
            throw new EmptyInputException("Name of the card holder must not be empty.");
        }

        if (!name.matches("^[a-zA-Z]+( [a-zA-Z-]+)*$")) {
            throw new InvalidInputFormatException("Name of the card holder must contain only letters, dashes, and spaces.");
        }

        if (name.length() > 20) {
            throw new InvalidInputFormatException("First and last name must be 20 characters or less.");
        }
    }

    public static void validateCardHolderAddress(String address) throws InvalidInputException {
        String[] addressParts = address.trim().split("\\s+");

        if (address.isEmpty() || address.trim().isEmpty()) {
            throw new EmptyInputException("Address must not be empty.");
        }

        if (!addressParts[0].matches("[0-9-]+")) {
            throw new InvalidInputFormatException("First part of the address must contain only numbers and dashes.");
        }

        for (int i = 1; i < addressParts.length; i++) {
            if (!addressParts[i].matches("^[A-Za-z,]*$")) {
                throw new InvalidInputFormatException("Excluding the first part of the address, address must contain only letters and commas.");
            }
        }
    }

    public static void validateCardHolderPostalCode(String postalCode) throws InvalidInputException {
        if (postalCode.isEmpty() || postalCode.trim().isEmpty()) {
            throw new EmptyInputException("Postal code must not be empty.");
        }

        if (!postalCode.matches("^[A-Z][0-9][A-Z] [0-9][A-Z][0-9]$")) {
            throw new InvalidInputFormatException("Postal code must be in the format of a Canadian postal code.");
        }
    }
}
