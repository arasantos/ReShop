package comp3350.reshop.tests.logic;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.Calendar;

import comp3350.reshop.logic.validation.PaymentValidator;
import comp3350.reshop.logic.exceptions.EmptyInputException;
import comp3350.reshop.logic.exceptions.ExpiredCardException;
import comp3350.reshop.logic.exceptions.InvalidInputFormatException;
import comp3350.reshop.logic.exceptions.UnsupportedCardTypeException;
import comp3350.reshop.tests.utils.TestLogging;

public class PaymentValidatorTest {
    private final Integer currentYearInt = (Calendar.getInstance().get(Calendar.YEAR)) % 100; // last 2 digits of current year
    private final Integer currentMonthInt = Calendar.getInstance().get(Calendar.MONTH) + 1;   // month starts at 0

    @Rule
    public TestRule logger = TestLogging.getTestLogger();

    @BeforeClass
    public static void startSuite() {
        System.out.println("\n===== Starting Payment Validator suite =====");
    }

    @AfterClass
    public static void endSuite() {
        System.out.println("===== Completed Payment Validator suite =====\n");
    }

    @Test
    public void testValidCardNumber() {
        boolean passed = true;
        try {
            PaymentValidator.validateCardNumber("4505 1234 5678 9000");
            PaymentValidator.validateCardNumber("5505 1234 5678 9000");
        } catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testEmptyCardNumber() {
        assertThrows("Should throw exception if the card number is empty",
                EmptyInputException.class,
                () -> PaymentValidator.validateCardNumber(""));

        assertThrows("Should throw exception if the card number is just spaces",
                EmptyInputException.class,
                () -> PaymentValidator.validateCardNumber(" "));
    }

    @Test
    public void testIncorrectCardNumberFormat() {
        assertThrows("Should throw exception if the card number is more than 16 digits",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validateCardNumber("4505 1234 5678 90000"));

        assertThrows("Should throw exception if the card number has too many spaces",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validateCardNumber("4505 1234 5678  9000"));
    }

    @Test
    public void testUnsupportedCardNumberType() {
        assertThrows("Should throw exception if the card number doesn't start with 4 nor 5, meaning it's neither VISA or Mastercard",
                UnsupportedCardTypeException.class,
                () -> PaymentValidator.validateCardNumber("6505 1234 5678 9000"));
    }

    @Test
    public void testValidCardExpiryDate() {
        boolean passed = true;
        try {
            String currentMonth = currentMonthInt + "";
            String nextMonth = "0" + (currentMonthInt + 1);
            String testYear = currentYearInt + "";

            if (currentMonthInt.toString().length() == 1) {
                currentMonth = "0" + currentMonth;
            }

            if (nextMonth.equals("13")) {
                nextMonth = "01";
                testYear = (currentYearInt + 1) + "";
            }

            PaymentValidator.validateCardExpiryDate(currentMonth + "/" + currentYearInt,  currentMonthInt, currentYearInt);
            PaymentValidator.validateCardExpiryDate(nextMonth + "/" + testYear, currentMonthInt, currentYearInt);
        } catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testEmptyCardExpiryDate() {
        assertThrows("Should throw exception if the card expiry date is empty",
                EmptyInputException.class,
                () -> PaymentValidator.validateCardExpiryDate("", currentMonthInt, currentYearInt));

        assertThrows("Should throw exception if the card expiry date is just spaces",
                EmptyInputException.class,
                () -> PaymentValidator.validateCardExpiryDate(" ", currentMonthInt, currentYearInt));
    }

    @Test
    public void testIncorrectCardExpiryDateFormat() {
        assertThrows("Should throw exception if the card expiry date doesn't have digits for MM/YY",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validateCardExpiryDate("aa/bb", currentMonthInt, currentYearInt));

        assertThrows("Should throw exception if the card expiry date doesn't follow MM/YY",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validateCardExpiryDate("12,30", currentMonthInt, currentYearInt));

        assertThrows("Should throw exception if the card expiry date's month is less than 1",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validateCardExpiryDate("00/30", currentMonthInt, currentYearInt));

        assertThrows("Should throw exception if the card expiry date's month is more than 12",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validateCardExpiryDate("13/30", currentMonthInt, currentYearInt));
    }

    @Test
    public void testExpiredExpiryDate() {
        assertThrows("Should throw exception if the card expiry date already passed",
                ExpiredCardException.class,
                () -> PaymentValidator.validateCardExpiryDate("12/23", currentMonthInt, currentYearInt));

        assertThrows("Should throw exception if the card expiry date already passed",
                ExpiredCardException.class,
                () -> PaymentValidator.validateCardExpiryDate("06/24", currentMonthInt, currentYearInt));
    }

    @Test
    public void testValidCVV() {
        boolean passed = true;
        try {
            PaymentValidator.validateCVV("123");
        } catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testEmptyCVV() {
        assertThrows("Should throw exception if the CVV is empty",
                EmptyInputException.class,
                () -> PaymentValidator.validateCVV(""));

        assertThrows("Should throw exception if the CVV is just spaces",
                EmptyInputException.class,
                () -> PaymentValidator.validateCVV(" "));
    }

    @Test
    public void testLengthyCVV() {
        assertThrows("Should throw exception if the CVV is more than 3 digits",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validateCVV("1234"));
    }

    @Test
    public void testCVVWithNonDigits() {
        assertThrows("Should throw exception if the CVV has letters",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validateCVV("12a"));

        assertThrows("Should throw exception if the CVV has special characters",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validateCVV("12,"));
    }

    @Test
    public void testValidPhoneNumber() {
        boolean passed = true;
        try {
            PaymentValidator.validatePhoneNumber("1234567890");
        } catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testEmptyPhoneNumber() {
        assertThrows("Should throw exception if the phone number is empty",
                EmptyInputException.class,
                () -> PaymentValidator.validatePhoneNumber(""));

        assertThrows("Should throw exception if the phone number is just spaces",
                EmptyInputException.class,
                () -> PaymentValidator.validatePhoneNumber(" "));
    }

    @Test
    public void testLengthyPhoneNumber() {
        assertThrows("Should throw exception if the phone number is more than 10 digits",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validatePhoneNumber("12345678901"));
    }

    @Test
    public void testPhoneNumberWithNonDigits() {
        assertThrows("Should throw exception if the phone number has letters",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validatePhoneNumber("1234567890a"));

        assertThrows("Should throw exception if the phone number has special characters",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validatePhoneNumber("1234567890,"));
    }

    @Test
    public void testValidCardHolderName() {
        boolean passed = true;
        try {
            PaymentValidator.validateCardHolderName("John Doe");
            PaymentValidator.validateCardHolderName("John Doe-Raemon");
        } catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testEmptyCardHolderName() {
        assertThrows("Should throw exception if the card holder's name is empty",
                EmptyInputException.class,
                () -> PaymentValidator.validateCardHolderName(""));

        assertThrows("Should throw exception if the card holder's name is just spaces",
                EmptyInputException.class,
                () -> PaymentValidator.validateCardHolderName(" "));
    }

    @Test
    public void testLengthyCardHolderName() {
        assertThrows("Should throw exception if the card holder's name is more than 20 characters",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validateCardHolderName("Beabadoobee Beabadoobee"));
    }

    @Test
    public void testCardHolderNameWithInvalidCharacters() {
        assertThrows("Should throw exception if the card holder's name has non-space or non-dash special characters",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validateCardHolderName("John Doe2.0"));
    }

    @Test
    public void testValidCardHolderAddress() {
        boolean passed = true;
        try {
            PaymentValidator.validateCardHolderAddress("1234 Something St, Somewhere, Canada");
            PaymentValidator.validateCardHolderAddress("12 Something St, Somewhere, Canada");
        } catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testEmptyCardHolderAddress() {
        assertThrows("Should throw exception if the card holder's address is empty",
                EmptyInputException.class,
                () -> PaymentValidator.validateCardHolderAddress(""));

        assertThrows("Should throw exception if the card holder's address is just spaces",
                EmptyInputException.class,
                () -> PaymentValidator.validateCardHolderAddress(" "));
    }

    @Test
    public void testIncorrectCardHolderAddressFormat() {
        assertThrows("Should throw exception if the card holder's address has a letter in the first part",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validateCardHolderAddress("1234A Something St, Somewhere, Canada"));

        assertThrows("Should throw exception if the card holder's address has invalid special characters",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validateCardHolderAddress("1234 Something St!, Somewhere, Canada"));
    }

    @Test
    public void testValidCardHolderPostalCode() {
        boolean passed = true;
        try {
            PaymentValidator.validateCardHolderPostalCode("A0A 0A0");
        } catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testEmptyCardHolderPostalCode() {
        assertThrows("Should throw exception if the card holder's postal code is empty",
                EmptyInputException.class,
                () -> PaymentValidator.validateCardHolderPostalCode(""));

        assertThrows("Should throw exception if the card holder's postal code is just spaces",
                EmptyInputException.class,
                () -> PaymentValidator.validateCardHolderPostalCode(" "));
    }

    @Test
    public void testIncorrectCardHolderPostalCode() {
        assertThrows("Should throw exception if the card holder's postal code doesn't follow LNL LNL format where L = letter and N = number",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validateCardHolderPostalCode("0A0 A0A"));

        assertThrows("Should throw exception if the card holder's postal code has non-space special characters",
                InvalidInputFormatException.class,
                () -> PaymentValidator.validateCardHolderPostalCode("A0A-0A0"));
    }
}
