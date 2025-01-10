package comp3350.reshop.tests.logic;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.rules.TestRule;

import comp3350.reshop.logic.validation.PostValidator;
import comp3350.reshop.logic.enums.Location;
import comp3350.reshop.logic.enums.Quality;
import comp3350.reshop.logic.enums.Style;
import comp3350.reshop.logic.enums.Type;
import comp3350.reshop.logic.exceptions.EmptyInputException;
import comp3350.reshop.logic.exceptions.InvalidChoiceException;
import comp3350.reshop.logic.exceptions.InvalidInputFormatException;
import comp3350.reshop.tests.utils.TestLogging;

public class PostValidatorTest {
    private String longName ="";

    @Rule
    public TestRule logger = TestLogging.getTestLogger();

    @BeforeClass
    public static void startSuite() {
        System.out.println("\n===== Starting Post Validator suite =====");
    }

    @Before
    public void setUp() {
        longName = fillLongName();
    }

    @AfterClass
    public static void endSuite() {
        System.out.println("===== Completed Post Validator suite =====\n");
    }

    @Test
    public void testValidDescription() {
        boolean passed = true;
        try {
            PostValidator.validateDescription("abcde,abcd ,a.");
            PostValidator.validateDescription("abcde,abcd ,a.\nsize: L");
        } catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testEmptyDescription() {
        assertThrows("Should throw exception if the description is empty",
                EmptyInputException.class,
                () -> PostValidator.validateDescription(""));

        assertThrows("Should throw exception if the description is just spaces",
                EmptyInputException.class,
                () -> PostValidator.validateDescription(" "));
    }

    @Test
    public void testTooLongDescription() {
        assertThrows("Should throw exception if the description is more than 500 characters",
                InvalidInputFormatException.class,
                () -> PostValidator.validateDescription(longName));
    }

    @Test
    public void testValidItemName() {
        boolean passed = true;
        try {
            PostValidator.validateItemName("abcde abcd a");
        } catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testTooLongItemName() {
        assertThrows("Should throw exception if the item name is more than 70 characters",
                InvalidInputFormatException.class,
                () -> PostValidator.validateItemName(longName));
    }

    @Test
    public void testEmptyItemName() {
        assertThrows("Should throw exception if the item name is empty",
                EmptyInputException.class,
                () -> PostValidator.validateItemName(""));

        assertThrows("Should throw exception if the item name is just spaces",
                EmptyInputException.class,
                () -> PostValidator.validateItemName(" "));
    }

    @Test
    public void testValidPrices() {
        boolean passed = true;
        try {
            PostValidator.validatePrice(1234);
        } catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testEmptyPrices() {
        assertThrows("Should throw exception if the price is 0",
                EmptyInputException.class,
                () -> PostValidator.validatePrice(0));
    }

    @Test
    public void testNegativePrices() {
        assertThrows("Should throw exception if the price is negative",
                InvalidInputFormatException.class,
                () -> PostValidator.validatePrice(-1234));
    }

    @Test
    public void testUpperBoundPrices() {
        assertThrows("Should throw exception if the price is higher than $1000.00",
                InvalidInputFormatException.class,
                () -> PostValidator.validatePrice(123400));
    }

    @Test
    public void testValidImage() {
        boolean passed = true;
        try {
            PostValidator.validateImageUriString("content://media/picker/0/com.android.providers.media.photopicker/media/1000000034");
        } catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testEmptyImage() {
        assertThrows("Should throw exception if the image uri string is empty",
                EmptyInputException.class,
                () -> PostValidator.validateImageUriString(""));

        assertThrows("Should throw exception if the image uri string is just spaces",
                EmptyInputException.class,
                () -> PostValidator.validateImageUriString(" "));

        assertThrows("Should throw exception if the image uri string is null",
                EmptyInputException.class,
                () -> PostValidator.validateImageUriString(null));
    }

    @Test
    public void testValidQuality() {
        boolean passed = true;
        try {
            PostValidator.validateChoice("New with Tags", "Quality", Quality.values());
            PostValidator.validateChoice("Used - Excellent", "Quality", Quality.values());
        } catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testEmptyQuality() {
        assertThrows("Should throw exception if the quality is empty",
                EmptyInputException.class,
                () -> PostValidator.validateChoice("", "Quality", Quality.values())
        );

        assertThrows("Should throw exception if the quality is just spaces",
                EmptyInputException.class,
                () -> PostValidator.validateChoice("  ", "Quality", Quality.values())
        );
    }

    @Test
    public void testNotPartOfChoicesQuality() {
        assertThrows("Should throw exception if the quality is not part of the choices",
                InvalidChoiceException.class,
                () -> PostValidator.validateChoice("LikeNew", "Quality", Quality.values())
        );
    }

    @Test
    public void testValidType() {
        boolean passed = true;
        try {
            PostValidator.validateChoice("Shoes", "Type", Type.values());
            PostValidator.validateChoice("Pants", "Type", Type.values());
        } catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testEmptyType() {
        assertThrows("Should throw exception if the type is empty",
                EmptyInputException.class,
                () -> PostValidator.validateChoice("", "Type", Type.values()));

        assertThrows("Should throw exception if the type is just spaces",
                EmptyInputException.class,
                () -> PostValidator.validateChoice(" ", "Type", Type.values()));
    }

    @Test
    public void testNotPartOfChoicesType() {
        assertThrows("Should throw exception if the type is not part of the choices",
                InvalidChoiceException.class,
                () -> PostValidator.validateChoice("vintage", "Type", Type.values()));

        assertThrows("Should throw exception if the type is not part of the choices",
                InvalidChoiceException.class,
                () -> PostValidator.validateChoice("CropTop", "Type", Type.values()));
    }

    @Test
    public void testValidStyle() {
        boolean passed = true;
        try {
            PostValidator.validateChoice("Preppy", "Style", Style.values());
        } catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testEmptyStyle() {
        assertThrows("Should throw exception if the style is empty",
                EmptyInputException.class,
                () -> PostValidator.validateChoice("", "Style", Style.values()));

        assertThrows("Should throw exception if the style is just spaces",
                EmptyInputException.class,
                () -> PostValidator.validateChoice(" ", "Style", Style.values()));
    }

    @Test
    public void testNotPartOfChoicesStyle() {
        assertThrows("Should throw exception if the style is not part of the choices",
                InvalidChoiceException.class,
                () -> PostValidator.validateChoice("vintage", "Style", Style.values()));

        assertThrows("Should throw exception if the style is not part of the choices",
                InvalidChoiceException.class,
                () -> PostValidator.validateChoice("cool stuff", "Style", Style.values()));
    }

    @Test
    public void testValidLocation() {
        boolean passed = true;
        try {
            PostValidator.validateChoice("Winnipeg", "Location", Location.values());
        } catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testEmptyLocation() {
        assertThrows("Should throw exception if the location is empty",
                EmptyInputException.class,
                () -> PostValidator.validateChoice("", "Location", Location.values()));

        assertThrows("Should throw exception if the location is just spaces",
                EmptyInputException.class,
                () -> PostValidator.validateChoice(" ", "Location", Location.values()));
    }

    @Test
    public void testNotPartOfChoicesLocation() {
        assertThrows("Should throw exception if the location is not part of the choices",
                InvalidChoiceException.class,
                () -> PostValidator.validateChoice("FlinFlon", "Location", Location.values()));

        assertThrows("Should throw exception if the location is not part of the choices",
                InvalidChoiceException.class,
                () -> PostValidator.validateChoice("Best", "Location", Location.values()));
    }

    private String fillLongName() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 501; i++) {
            builder.append("a");
        }

        return builder.toString();
    }
}