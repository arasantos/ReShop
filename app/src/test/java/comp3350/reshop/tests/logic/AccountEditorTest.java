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
import comp3350.reshop.logic.AccountEditor;
import comp3350.reshop.logic.exceptions.InvalidInputFormatException;
import comp3350.reshop.logic.exceptions.InvalidUsernameException;
import comp3350.reshop.objects.User;
import comp3350.reshop.tests.utils.TestLogging;

public class AccountEditorTest {

	private AccountEditor accountEditor;
	private UserData db;

	@Rule
	public TestRule logger = TestLogging.getTestLogger();

	@BeforeClass
	public static void startSuite() {
		System.out.println("\n===== Starting Account Editor suite =====");
	}

	@Before
	public void setUp() {
		db = mock();
		accountEditor = new AccountEditor(db);
	}

	@AfterClass
	public static void endSuite() {
		System.out.println("===== Completed Account Editor suite =====\n");
	}

	@Test
	public void testCreateNewUser() {
		boolean passed = true;
		User user = new User("username", "password",
				"first", "last", "Winnipeg");

		try {
			accountEditor.createNewAccount(user);
		} catch (Exception e) {
			passed = false;
		}

		assertTrue(passed);
	}

	@Test
	public void testEditExistingUser() {
		boolean passed = true;
		User user = new User("daviest", "password",
				"test", "Davies", "Winnipeg");

		try {
			accountEditor.updateAccount(user);
		} catch (Exception e) {
			passed = false;
		}

		assertTrue(passed);
	}

	@Test
	public void testCreateInvalidUser() {
		User duplicate = new User("daviest", "password",
				"tristan", "Davies", "Winnipeg");
		when(db.getUser(duplicate.getUsername())).thenReturn(duplicate);

		assertThrows("Should fail to create a user if the user already exists",
				InvalidUsernameException.class, () -> accountEditor.createNewAccount(duplicate));
	}

	@Test
	public void testUpdateInvalidUser() {
		User user = new User("daviest", "password",
				"123", "Davies", "Winnipeg");

		assertThrows("Should fail to update the user if the data is invalid",
				InvalidInputFormatException.class, () -> accountEditor.updateAccount(user));
	}
}
