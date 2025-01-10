package comp3350.reshop.tests.logic;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import comp3350.reshop.logic.util.PaymentBuilder;
import comp3350.reshop.objects.Payment;
import comp3350.reshop.tests.utils.TestLogging;


public class PaymentBuilderTest {

    @Rule
    public TestRule logger = TestLogging.getTestLogger();

    @BeforeClass
    public static void startSuite() {
        System.out.println("===== Starting payment builder test =====");
    }

    @AfterClass
    public static void endSuite() {
        System.out.println("===== Completed payment builder test =====");
    }

    @Test
    public void buildPayment() {
        PaymentBuilder builder = new PaymentBuilder();

        builder.setCardNumber("1234 1234 1234 1234");
        builder.setExpiry("11/11");
        builder.setCvv("111");
        builder.setName("Test User");
        builder.setAddress("111 Baker St");
        builder.setPostalCode("1A2 B3C");
        builder.setPhoneNumber("1234567890");

        Payment result = builder.getProduct();

        assertEquals("Builder should set the card number",
                "1234 1234 1234 1234", result.getCardNumber());
        assertEquals("Builder should set the expiry date",
                "11/11", result.getExpiry());
        assertEquals("Builder should set the cvv",
                "111", result.getCvv());
        assertEquals("Builder should set the cardholder name",
                "Test User", result.getName());
        assertEquals("Builder should set the holder address",
                "111 Baker St", result.getAddress());
        assertEquals("Builder should set the postal code",
                "1A2 B3C", result.getPostalCode());
        assertEquals("Builder should set phone number",
                "1234567890", result.getPhoneNumber());
    }


}
