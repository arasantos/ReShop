package comp3350.reshop;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;

import android.widget.TextView;

import com.google.android.material.textview.MaterialTextView;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import comp3350.reshop.presentation.LoginActivity;

@RunWith(AndroidJUnit4.class)
public class CheckoutSystemTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);

    private static String username;
    private static String password;
    private static String currentMonth;
    private static String year;
    private static String itemName1;

    @BeforeClass
    public static void setUpBeforeClass() {
        username = "santosa";
        password = "password";
        itemName1 = "A T-Shirt";

        Integer currentYearInt = (Calendar.getInstance().get(Calendar.YEAR)) % 100; // last 2 digits of current year
        Integer currentMonthInt = Calendar.getInstance().get(Calendar.MONTH) + 1;   // month starts at 0

        currentMonth = currentMonthInt + "";
        year = currentYearInt + "";

        if (currentMonthInt.toString().length() == 1) {
            currentMonth = "0" + currentMonth;
        }
    }

    @BeforeClass
    public static void startSuite() {
        System.out.println("\n===== Starting Checkout System Test suite =====");
    }

    @AfterClass
    public static void endSuite() {
        System.out.println("===== Completed Checkout System Test suite =====\n");
    }


    /**
     * Acceptance Test for:
     *      As a buyer, I want to enter my payment information directly on the app
     *      so that I can purchase clothing items without interrupting my shopping experience
     * <p>
     *      As a buyer, I want to see a history of my purchases so that I can track which clothing
     *      items I have already bought.
     * <p>
     * 1. Log in to the app as a user using santosa (username) and password (password)
     * 2. Click an item on the browse page
     * 3. Verify that the item isn't sold by you by checking the seller
     * 4. Click the buy button
     * 5. Type in the information it's asking for, follow the instructions that show up for invalid payment information
     * 6. Click the buy button
     * 7. Click the account button
     * 8. Verify that you bought the item by seeing if the same item shows up in bought items (purchase history) on Account page
     * 9. Click the account button to see log out button
     * 10. Log out
     */
    @Test
    public void testPayingForItem() {
        // Log in to the app as a user using santosa (username) and password (password)
        onView(withId(R.id.usernameInput)).perform(typeText(username));
        onView(withId(R.id.passwordInput)).perform(typeText(password));
        closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());

        // Click an item on the browse page
        onView(withId(R.id.searchContent)).perform(actionOnItemAtPosition(0,click()));

        // Verify that the item isn't sold by you by checking the seller
        onView(withId(R.id.sellerTextView)).check(matches(not(withText(username))));

        // (Verify that the first item is selected)
        verifyFirstItem();

        // Click the buy button
        onView(withId(R.id.buyButtonPost)).perform(click());

        // Type in the information it's asking for, follow the instructions that show up for invalid payment information
        onView(withId(R.id.cardNumberInput)).perform(typeText("5234 1234 1234 1234"));
        onView(withId(R.id.expiryInput)).perform(typeText(currentMonth + "/" + year));
        onView(withId(R.id.cvvInput)).perform(typeText("123"));
        onView(withId(R.id.nameOnCardInput)).perform(typeText("John Doe"));
        onView(withId(R.id.addressInput)).perform(typeText("123 Somewhere Street"));
        onView(withId(R.id.locationDropdown)).perform(click());
        onView(withText("Winnipeg")).perform(click());
        onView(withId(R.id.locationDropdown)).check(matches(withSpinnerText(containsString("Winnipeg"))));
        onView(withId(R.id.countryText)).check(matches(withText("Canada")));
        onView(withId(R.id.postalCodeInput)).perform(typeText("A0A 0A0"));
        onView(withId(R.id.phoneNumberInput)).perform(typeText("1234567890"));
        closeSoftKeyboard();

        // Click the buy button
        onView(withId(R.id.buyButton)).perform(click());

        // Click the account button
        onView(withId(R.id.nav_action_account)).perform(click());

        // Verify that you bought the item by seeing if the same item shows up in bought items on Account page
        onView(withId(R.id.showBoughtItemsButton)).perform(click());
        onView(withId(R.id.boughtItemsRecyclerView)).perform(actionOnItemAtPosition(0,click()));
        verifyFirstItem();

        // Click the "Account" button to see log out button
        onView(withId(R.id.nav_action_account)).perform(click());

        // Log out
        onView(withId(R.id.LogoutButton)).perform(click());
    }

    private void verifyFirstItem() {
        onView(allOf(anyOf(instanceOf(TextView.class), instanceOf(MaterialTextView.class)), withText(itemName1)))
                .check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.locationTextView)).check(matches(not(withText("Brandon"))));
        onView(withId(R.id.typeTextView)).check(matches(not(withText("Crop Top"))));
        onView(withId(R.id.locationTextView)).check(matches(not(withText("Vintage"))));
        onView(withId(R.id.typeTextView)).check(matches(not(withText("Like New"))));
        onView(withId(R.id.locationTextView)).check(matches(not(withText("1999"))));
        onView(withId(R.id.typeTextView)).check(matches(not(withText("daviest"))));
    }
}
