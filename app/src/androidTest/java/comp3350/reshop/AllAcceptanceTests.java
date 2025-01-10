package comp3350.reshop;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        FilterSystemTest.class,
        SortSystemTest.class,
        CheckoutSystemTest.class,
})
public class AllAcceptanceTests {
}