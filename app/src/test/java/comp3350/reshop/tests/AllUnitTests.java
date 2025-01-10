package comp3350.reshop.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.reshop.tests.data.ClothingItemAccessorTest;
import comp3350.reshop.tests.logic.AccountEditorTest;
import comp3350.reshop.tests.logic.AccountValidatorTest;
import comp3350.reshop.tests.logic.ClothingItemBuilderTest;
import comp3350.reshop.tests.logic.LoginManagerTest;
import comp3350.reshop.tests.logic.PaymentBuilderTest;
import comp3350.reshop.tests.logic.PaymentManagerTest;
import comp3350.reshop.tests.logic.PaymentValidatorTest;
import comp3350.reshop.tests.logic.PostSearcherTest;
import comp3350.reshop.tests.logic.PostValidatorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    AccountEditorTest.class,
    AccountValidatorTest.class,
    ClothingItemBuilderTest.class,
    LoginManagerTest.class,
    PaymentBuilderTest.class,
    PaymentManagerTest.class,
    PaymentValidatorTest.class,
    PostSearcherTest.class,
    PostValidatorTest.class,
    ClothingItemAccessorTest.class,
})
public class AllUnitTests
{

}
