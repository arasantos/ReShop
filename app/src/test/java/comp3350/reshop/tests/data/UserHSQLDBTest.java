package comp3350.reshop.tests.data;

import static org.junit.Assert.*;

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
import comp3350.reshop.data.hsqldb.DataException;
import comp3350.reshop.data.hsqldb.UserHSQLDB;
import comp3350.reshop.objects.User;
import comp3350.reshop.tests.utils.TestDB;
import comp3350.reshop.tests.utils.TestLogging;

public class UserHSQLDBTest {
    private UserHSQLDB users;
    private File tempDB;
    private final User testUser = new User( "dunkinDonut", "ILoveDonut.001", "Dragon", "Ball", "Brandon");

    @Rule
    public TestRule logger = TestLogging.getTestLogger();

    @BeforeClass
    public static void startSuite() {
        System.out.println("\n===== Starting User HSQLDB integration suite =====");
    }

    @Before
    public void setUp() throws IOException {
        this.tempDB = TestDB.copyDB();
        this.users = new UserHSQLDB(Main.getDBPathName());
    }

    @AfterClass
    public static void endSuite() {
        System.out.println("===== Completed User HSQLDB integration  suite =====\n");
    }

    @Test
    public void testInsertUser() {
        User user1;
        user1 = new User( "MichealJackson", "Music123!", "Micheal", "Jackson", "Brandon");
        users.insertUser(user1);
        assertNotNull("Should insert a new user into the database", users.getUser("MichealJackson"));
    }

    @Test
    public void testGetUser() {
        User user1;
        user1 = users.getUser("daviest");
        assertNotNull(user1);
    }
    @Test
    public void testUpdateUser() {
        User user1=new User( "CottonCandy27", "notMyPassword", "Blake", "Lively", "Winnipeg");
        users.insertUser(user1);
        users.updateUser(user1.getUsername(), testUser);
        assertNotEquals(user1.getUsername(), testUser.getUsername());
        assertEquals("ILoveDonut.001",testUser.getPassword());
        assertEquals("Dragon",testUser.getFirstName());
        assertEquals("Ball",testUser.getLastName());
        assertEquals("Brandon",testUser.getCity());
    }

    @Test
    public void testInvalidInsertion() {
        User invalidUser = new User("usernameusernameusernameusernameusernameusername", "password",
                "name", "name", "city");

        assertThrows("Throws exception if username is over 30 characters",
                DataException.class,
                () -> users.insertUser(invalidUser));
    }

    @Test
    public void testInvalidUpdate() {
        User invalidUser = new User("validUsername", "passwordpasswordpasswordpassword",
                "name", "name", "city");
        users.insertUser(testUser);

        assertThrows("Throws exception if the data strings are over 20 characters",
                DataException.class,
                () -> users.updateUser(testUser.getUsername(), invalidUser));
    }

    @Test
    public void testNonExistentUser() {
        User user;

        user = users.getUser("doesnotexist");
        assertNull("Should return null if no username exists in database", user);
    }

    @After
    public void tearDown() {
        this.tempDB.delete();
    }
}

