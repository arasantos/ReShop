package comp3350.reshop.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.reshop.tests.data.ClothingItemAccessorIT;
import comp3350.reshop.tests.data.UserHSQLDBTest;
import comp3350.reshop.tests.logic.AccountEditorIT;
import comp3350.reshop.tests.logic.LoginManagerIT;
import comp3350.reshop.tests.logic.PaymentManagerIT;
import comp3350.reshop.tests.logic.PostSearcherTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		AccountEditorIT.class,
		LoginManagerIT.class,
		PaymentManagerIT.class,
		PostSearcherTest.class,
        ClothingItemAccessorIT.class,
		UserHSQLDBTest.class
})
public class AllIntegrationTests {
}