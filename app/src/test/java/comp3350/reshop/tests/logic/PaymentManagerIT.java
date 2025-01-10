package comp3350.reshop.tests.logic;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import comp3350.reshop.application.Main;
import comp3350.reshop.data.hsqldb.ClothingItemHSQLDB;
import comp3350.reshop.logic.PaymentManager;
import comp3350.reshop.logic.enums.Location;
import comp3350.reshop.logic.enums.Quality;
import comp3350.reshop.logic.enums.Style;
import comp3350.reshop.logic.enums.Type;
import comp3350.reshop.logic.exceptions.InvalidInputException;
import comp3350.reshop.logic.exceptions.InvalidInputFormatException;
import comp3350.reshop.logic.exceptions.InvalidItemException;
import comp3350.reshop.logic.util.ClothingItemBuilder;
import comp3350.reshop.logic.util.PaymentBuilder;
import comp3350.reshop.objects.ClothingItem;
import comp3350.reshop.objects.Payment;
import comp3350.reshop.tests.utils.TestDB;
import comp3350.reshop.tests.utils.TestLogging;

public class PaymentManagerIT {
    private String currentUser;
    private Payment paymentValid;
    private PaymentManager paymentManager;
    private ClothingItemHSQLDB clothingItems;
    private File tempDB;

    @Rule
    public TestRule logger = TestLogging.getTestLogger();

    @BeforeClass
    public static void startSuite() {
        System.out.println("\n===== Starting Payment Manager integration suite =====");
    }

    @Before
    public void setUp() throws IOException {
        ClothingItem item = buildItem(6, null);
        ClothingItem itemWithBuyer = buildItem(7, "santosa");

        this.tempDB = TestDB.copyDB();
        this.clothingItems = new ClothingItemHSQLDB(Main.getDBPathName(), new ClothingItemBuilder());

        paymentManager = new PaymentManager(clothingItems);
        paymentValid = buildPayment("Test Pass");

        clothingItems.addItem(item);
        clothingItems.addItem(itemWithBuyer);
        currentUser = "daviest";
    }

    @AfterClass
    public static void endSuite() {
        System.out.println("===== Completed Payment Manager integration suite =====\n");
    }

    @Test
    public void testProcessPaymentValid() {
        boolean passed = true;

        try {
            paymentManager.processPayment(paymentValid, 6, currentUser);
        } catch (InvalidInputException e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testInvalidPayment() {
        Payment paymentInvalid = buildPayment("Test Fail123");
        assertThrows("Should throw an exception if the payment isn't successful (eg. name is invalid)",
                InvalidInputFormatException.class,
                () -> paymentManager.processPayment(paymentInvalid, 6, currentUser)
        );
    }

    @Test
    public void testSoldItem() {
        assertThrows("Should throw an exception if the item has already been sold",
                InvalidItemException.class,
                () -> paymentManager.processPayment(paymentValid, 7, currentUser)
        );
    }

    @Test
    public void testUnavailableItem() {
        assertThrows("Should throw an exception if the item doesn't exist",
                InvalidItemException.class,
                () -> paymentManager.processPayment(paymentValid, 8, currentUser)
        );
    }

    @Test
    public void testInvalidID() {
        assertThrows("Should throw an exception if the item id is negative",
                InvalidItemException.class,
                () -> paymentManager.processPayment(paymentValid, -1, currentUser)
        );
    }

    private Payment buildPayment(String name) {
        PaymentBuilder builder = new PaymentBuilder();
        int currentYearInt = (Calendar.getInstance().get(Calendar.YEAR)) % 100;     // last 2 digits of current year
        Integer currentMonthInt = Calendar.getInstance().get(Calendar.MONTH) + 1;   // month starts at 0
        String currentMonth = currentMonthInt + "";

        if (currentMonthInt.toString().length() == 1) {
            currentMonth = "0" + currentMonth;
        }

        builder.setCardNumber("5234 1234 1234 1234");
        builder.setExpiry(currentMonth + "/" + currentYearInt);
        builder.setCvv("111");
        builder.setName(name);
        builder.setAddress("111 Baker St");
        builder.setPostalCode("A0A 0A0");
        builder.setPhoneNumber("1234567890");

        return builder.getProduct();
    }

    private ClothingItem buildItem(int id, String buyer) {
        ClothingItemBuilder builder = new ClothingItemBuilder();
        builder.setId(id);
        builder.setName("name");
        builder.setDescription("description");
        builder.setLocation(Location.Brandon.toString());
        builder.setType(Type.Sandals.toString());
        builder.setStyle(Style.Preppy.toString());
        builder.setQuality(Quality.LikeNew.toString());
        builder.setPrice(1000);
        builder.setImageUri("content://media/picker/0/com.android.providers.media.photopicker/media/1000000034");
        builder.setSeller("sunh");
        builder.setBuyer(buyer);
        return builder.getProduct();
    }
    @After
    public void tearDown() {
        this.tempDB.delete();
    }
}
