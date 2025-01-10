package comp3350.reshop;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import android.view.View;
import android.view.ViewGroup;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp3350.reshop.presentation.LoginActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SortSystemTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> loginRule = new ActivityScenarioRule<>(LoginActivity.class);

    @BeforeClass
    public static void startSuite() {
        System.out.println("\n===== Starting Sorting System Test suite =====");
    }

    @AfterClass
    public static void endSuite() {
        System.out.println("===== Completed Sorting System Test suite =====\n");
    }

    /**
     * Acceptance Test:
     * 1. Log in to the app as a user using hongj (username) and password (password)
     * 2. Select a sort option from the dropdown
     * 3. Verify that the items are sorted
     * 4. Log out
     */
    @Test
    public void testSortItems() {
        // Login using user credentials
        // username: hongj
        // password: password
        onView(withId(R.id.usernameInput)).perform(typeText("hongj"));
        onView(withId(R.id.passwordInput)).perform(typeText("password"));
        onView(withId(R.id.loginButton)).perform(click());

        // Select a sort option
        onView(withId(R.id.SortSpinner)).perform(click());
        onView(withText("Price: Low to High")).perform(click());

        // Verify that the items have been sorted
        onView(nthChildOf(withId(R.id.searchContent), 0)).check(matches(hasDescendant(withText("$9.10"))));
        onView(nthChildOf(withId(R.id.searchContent), 1)).check(matches(hasDescendant(withText("$19.99"))));
        onView(nthChildOf(withId(R.id.searchContent), 2)).check(matches(hasDescendant(withText("$29.99"))));

        // Logout
        onView(withId(R.id.nav_action_account)).perform(click());
        onView(withId(R.id.LogoutButton)).perform(click());
    }

    /*
     * Helper Method
     * returns a Matcher that can find the child View at position `childPosition` within the `parentMatcher`.
     */
    private Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with "+childPosition+" child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
            }
        };
    }


}