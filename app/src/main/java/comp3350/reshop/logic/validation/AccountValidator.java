package comp3350.reshop.logic.validation;

import comp3350.reshop.data.UserData;
import comp3350.reshop.logic.EnumUtils;
import comp3350.reshop.logic.enums.Location;
import comp3350.reshop.logic.exceptions.EmptyInputException;
import comp3350.reshop.logic.exceptions.InvalidChoiceException;
import comp3350.reshop.logic.exceptions.InvalidInputException;
import comp3350.reshop.logic.exceptions.InvalidInputFormatException;
import comp3350.reshop.logic.exceptions.InvalidUsernameException;

public final class AccountValidator {

	private AccountValidator() {
	}

	public static void validateUsername(String username,UserData database) throws InvalidInputException {
		if (username.isEmpty() || username.trim().isEmpty()) {
			throw new EmptyInputException("Username is empty.");
		}

		if (database.getUser(username) != null) {
			throw new InvalidUsernameException("Username already exists.");
		}

		if (!username.matches("[A-Za-z0-9]+")) {
			throw new InvalidInputFormatException("Username must contain only letters and numbers.");
		}

		if (username.length() > 30) {
			throw new InvalidInputFormatException("Username must be 30 characters or less.");
		}
	}

	public static void validatePassword(String password) throws InvalidInputException {

		if (password.isEmpty() || password.trim().isEmpty()) {
			throw new EmptyInputException("Password is empty.");
		}

		if (!password.matches("[A-Za-z0-9]+")) {
			throw new InvalidInputFormatException("Password must contain only letters and numbers.");
		}

		if (password.length() > 20) {
			throw new InvalidInputFormatException("Password must be 20 characters or less");
		}
	}

	public static void validateName(String name) throws InvalidInputException {
		if (name.isEmpty() || name.trim().isEmpty()) {
			throw new EmptyInputException("First or last name is empty.");
		}

		if (!name.matches("^[a-zA-Z]+( [a-zA-Z]+)*$")) {
			throw new InvalidInputFormatException("First and last name must contain only letters.");
		}

		if (name.length() > 20) {
			throw new InvalidInputFormatException("First and last name must be 20 characters or less.");
		}
	}

	public static void validateCity(String city) throws InvalidInputException {
		if (city.isEmpty() || city.trim().isEmpty()) {
			throw new EmptyInputException("City is empty.");
		}

		if (!city.matches("^[a-zA-Z]+( [a-zA-Z]+)*$")) {
			throw new InvalidInputFormatException("City must contain only letters.");
		}

		if (city.length() > 20) {
			throw new InvalidInputFormatException("City must be 20 characters or less.");
		}

		if (EnumUtils.isMissingFromEnums(city, Location.values())) {
			throw new InvalidChoiceException("City must be one of the choices.");
		}
	}
}
