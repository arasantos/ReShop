package comp3350.reshop.tests.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import comp3350.reshop.data.UserData;
import comp3350.reshop.logic.LoginManager;
import comp3350.reshop.logic.exceptions.InvalidInputException;
import comp3350.reshop.logic.exceptions.InvalidPasswordException;
import comp3350.reshop.logic.exceptions.InvalidUsernameException;
import comp3350.reshop.objects.User;
import comp3350.reshop.tests.utils.TestLogging;

public class LoginManagerTest {

    private LoginManager loginManager;
    private UserData db;
    private final User validUser = new User("daviest", "password",
            "first", "last", "city");

    @Rule
    public TestRule logger = TestLogging.getTestLogger();

    @BeforeClass
    public static void startSuite() {
        System.out.println("\n===== Starting Login Manager suite =====");
    }

    @Before
    public void setUp() {
        db = mock();
        when(db.getUser("daviest")).thenReturn(validUser);
        loginManager = new LoginManager(db);
    }

    @AfterClass
    public static void endSuite() {
        System.out.println("===== Completed Login Manager suite =====\n");
    }

    @Test
    public void testNonExistentUsername() {
        assertThrows("Should throw an exception if the username doesn't exist",
                InvalidUsernameException.class,
                () -> loginManager.validateLogin("doesNotExist", "123")
        );
    }

    @Test
    public void testWrongPassword() {
        assertThrows("Should throw an exception if the password is incorrect",
                InvalidPasswordException.class,
                () -> loginManager.validateLogin("daviest", "incorrect")
        );
    }

    @Test
    public void testSuccessfulLogin() {
        boolean passed = true;
        try {
            loginManager.validateLogin("daviest", "password");
        } catch (InvalidInputException e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void testGetUser() {
        assertEquals("Should return the user if it exists",
                "daviest", loginManager.getUser("daviest").getUsername());
    }

    @Test
    public void testUserDNE() {
        when(db.getUser("DNE")).thenReturn(null);
        assertNull("Should return null if the user does not exist",
                loginManager.getUser("DNE"));
    }
}
