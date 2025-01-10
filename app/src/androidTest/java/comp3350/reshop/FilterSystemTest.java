package comp3350.reshop;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp3350.reshop.presentation.LoginActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FilterSystemTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> loginRule = new ActivityScenarioRule<>(LoginActivity.class);

    @BeforeClass
    public static void startSuite() {
        System.out.println("\n===== Starting Filter System Test suite =====");
    }

    @AfterClass
    public static void endSuite() {
        System.out.println("===== Completed Filter System Test suite =====\n");
    }

    /**
     * Acceptance Test:
     * 1. Log in to the app as a user using hongj (username) and password (password)
     * 2. Click on a filter chip to filter the items
     * 3. Verify that the items have been filtered
     * 4. Log out
     */
    @Test
    public void testFilterItemsByType() {
        // Login using user credentials
        // username: hongj
        // password: password
        onView(withId(R.id.usernameInput)).perform(typeText("hongj"));
        onView(withId(R.id.passwordInput)).perform(typeText("password"));
        onView(withId(R.id.loginButton)).perform(click());

        // Press a filter chip
        onView(allOf(withId(1), withText("T-shirt"))).perform(click());

        // Verify that the items have been filtered
        onView(withId(R.id.searchContent)).check(new RecyclerViewItemCountAssertion(1));
        onView(withId(R.id.searchContent)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText("C Sweater")))).check(matches(isDisplayed()));

        // Logout
        onView(withId(R.id.nav_action_account)).perform(click());
        onView(withId(R.id.LogoutButton)).perform(click());
    }

    /**
     * Acceptance Test:
     * 1. Log in to the app as a user using hongj (username) and password (password)
     * 2. Fill in the price range
     * 3. Press the enter button on the keyboard
     * 3. Verify that the items have been filtered
     * 4. Log out
     */
    @Test
    public void testFilterItemsByPrice() {
        // Login using user credentials
        // username: hongj
        // password: password
        onView(withId(R.id.usernameInput)).perform(typeText("hongj"));
        onView(withId(R.id.passwordInput)).perform(typeText("password"));
        onView(withId(R.id.loginButton)).perform(click());

        // Fill in the price filters
        // Press the enter button on the keyboard
        onView(withId(R.id.maxInput)).perform(typeText("20\n"), closeSoftKeyboard());

        // Verify that the items have been filtered
        onView(withId(R.id.searchContent)).check(new RecyclerViewItemCountAssertion(2));
        onView(withId(R.id.searchContent)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText("A T-Shirt")))).check(matches(isDisplayed()));
        onView(withId(R.id.searchContent)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText("E Shorts")))).check(matches(isDisplayed()));

        // Logout
        onView(withId(R.id.nav_action_account)).perform(click());
        onView(withId(R.id.LogoutButton)).perform(click());
    }
}
