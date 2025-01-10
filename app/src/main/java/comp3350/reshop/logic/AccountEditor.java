package comp3350.reshop.logic;

import comp3350.reshop.application.Service;
import comp3350.reshop.data.UserData;
import comp3350.reshop.logic.exceptions.InvalidInputException;
import comp3350.reshop.logic.validation.AccountValidator;
import comp3350.reshop.objects.User;

public class AccountEditor {

	private final UserData userData;

	public AccountEditor() {
		userData = Service.getUserData();
	}

	public AccountEditor(UserData userData) {
		this.userData = userData;
	}

	public void createNewAccount(User user) throws InvalidInputException {
		AccountValidator.validateUsername(user.getUsername(),userData);
		validateEditableUserData(user);

		userData.insertUser(user);
	}

	public void updateAccount(User user) throws InvalidInputException {
		validateEditableUserData(user);
		userData.updateUser(user.getUsername(), user);
	}

	private void validateEditableUserData(User user) throws InvalidInputException {
		AccountValidator.validatePassword(user.getPassword());
		AccountValidator.validateName(user.getFirstName());
		AccountValidator.validateName(user.getLastName());
		AccountValidator.validateCity(user.getCity());
	}
}
