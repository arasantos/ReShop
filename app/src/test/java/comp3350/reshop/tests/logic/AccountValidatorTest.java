package comp3350.reshop.tests.logic;

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
import comp3350.reshop.data.hsqldb.UserHSQLDB;
import comp3350.reshop.logic.validation.AccountValidator;
import comp3350.reshop.logic.exceptions.EmptyInputException;
import comp3350.reshop.logic.exceptions.InvalidInputFormatException;
import comp3350.reshop.logic.exceptions.InvalidUsernameException;
import comp3350.reshop.tests.utils.TestDB;
import comp3350.reshop.tests.utils.TestLogging;

public class AccountValidatorTest {
	private UserHSQLDB userData;
	private File tempDB;

	@Rule
	public TestRule logger = TestLogging.getTestLogger();

	@BeforeClass
	public static void startSuite() {
		System.out.println("\n===== Starting Account Validator suite =====");
	}
	@Before
	public void setUp() throws IOException {
		this.tempDB = TestDB.copyDB();
		this.userData = new UserHSQLDB(Main.getDBPathName());
	}

	@AfterClass
	public static void endSuite() {
		System.out.println("===== Completed Account Validator suite =====\n");
	}

	@Test
	public void testExistingUsername() {
		assertThrows("Should throw exception if the username already exists",
				InvalidUsernameException.class, () -> AccountValidator.validateUsername("daviest",userData));
	}

	@Test
	public void testEmptyUsername() {
		assertThrows("Should throw exception if the username is empty",
				EmptyInputException.class, () -> AccountValidator.validateUsername("",userData));
	}

	@Test
	public void testBlankUsername() {
		assertThrows("Should throw exception if the username is only whitespace",
				EmptyInputException.class, () -> AccountValidator.validateUsername("   ",userData));
	}

	@Test
	public void testInvalidUsername() {
		assertThrows("Should throw exception if the username is not alphanumeric",
				InvalidInputFormatException.class,
				() ->AccountValidator.validateUsername("not alphnumeric.",userData));
	}

	@Test
	public void testLongUsername() {
		assertThrows("Should throw exception if the username is over 30 chars",
				InvalidInputFormatException.class,
				() -> AccountValidator.validateUsername("exceptionexceptionexceptionexception",userData));
	}

	@Test
	public void testEmptyPassword() {
		assertThrows("Should throw exception if the password is empty",
				EmptyInputException.class, () -> AccountValidator.validatePassword(""));
	}

	@Test
	public void testBlankPassword() {
		assertThrows("Should throw exception if the password is only whitespace",
				EmptyInputException.class, () -> AccountValidator.validatePassword("   "));
	}

	@Test
	public void testInvalidPassword() {
		assertThrows("Should throw exception if the password is not alphanumeric",
				InvalidInputFormatException.class,
				() -> AccountValidator.validatePassword("not alphnumeric."));
	}

	@Test
	public void testTooLongPassword() {
		assertThrows("Should throw exception if password is over 20 chars",
				InvalidInputFormatException.class,
				() -> AccountValidator.validatePassword("exceptionexceptionexceptionexception"));
	}

	@Test
	public void testEmptyName() {
		assertThrows("Should throw exception if the name is empty",
				EmptyInputException.class, () -> AccountValidator.validateName(""));
	}

	@Test
	public void testBlankName() {
		assertThrows("Should throw exception if the name is only whitespace",
				EmptyInputException.class, () -> AccountValidator.validateName("   "));
	}

	@Test
	public void testInvalidName() {
		assertThrows("Should throw exception if the name is not alphabetic",
				InvalidInputFormatException.class,
				() -> AccountValidator.validateName("1234"));
	}

	@Test
	public void testTooLongName() {
		assertThrows("Should throw exception if the name is over 20 chars",
				InvalidInputFormatException.class,
				() -> AccountValidator.validateName("exceptionexceptionexceptionexception"));
	}

	@Test
	public void testEmptyCity() {
		assertThrows("Should throw exception if the city is empty",
				EmptyInputException.class, () -> AccountValidator.validateCity(""));
	}

	@Test
	public void testBlankCity() {
		assertThrows("Should throw exception if the city is only whitespace",
				EmptyInputException.class, () -> AccountValidator.validateCity("    "));
	}

	@Test
	public void testInvalidCity() {
		assertThrows("Should throw exception if the city is not alphabetic",
				InvalidInputFormatException.class,
				() -> AccountValidator.validateCity("1234"));
	}

	@Test
	public void testTooLongCity() {
		assertThrows("Should throw exception if the city is over 20 chars",
				InvalidInputFormatException.class,
				() -> AccountValidator.validateCity("exceptionexceptionexceptionexception"));

	}

	@After
	public void tearDown() {
		this.tempDB.delete();
	}
}
