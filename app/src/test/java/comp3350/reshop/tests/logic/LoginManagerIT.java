package comp3350.reshop.tests.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

import comp3350.reshop.application.Main;
import comp3350.reshop.data.hsqldb.UserHSQLDB;
import comp3350.reshop.logic.LoginManager;
import comp3350.reshop.logic.exceptions.InvalidInputException;
import comp3350.reshop.logic.exceptions.InvalidPasswordException;
import comp3350.reshop.logic.exceptions.InvalidUsernameException;
import comp3350.reshop.tests.utils.TestDB;
import comp3350.reshop.tests.utils.TestLogging;

public class LoginManagerIT {
    private LoginManager loginManager;
    private File tempDB;
    private UserHSQLDB users;

    @Rule
    public TestRule logger = TestLogging.getTestLogger();

    @BeforeClass
    public static void startSuite() {
        System.out.println("\n===== Starting Login Manager integration suite =====");
    }

    @Before
    public void setUp() throws IOException {
        this.tempDB = TestDB.copyDB();
        this.users = new UserHSQLDB(Main.getDBPathName());
        this.loginManager = new LoginManager(users);
    }

    @AfterClass
    public static void endSuite() {
        System.out.println("===== Completed Login Manager integration  suite =====\n");
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
        assertNull("Should return null if the user does not exist",
                loginManager.getUser("DNE"));
    }

    @After
    public void tearDown() {
        this.tempDB.delete();
    }
}
