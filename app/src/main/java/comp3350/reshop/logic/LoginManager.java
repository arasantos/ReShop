package comp3350.reshop.logic;

import comp3350.reshop.logic.exceptions.InvalidInputException;
import comp3350.reshop.logic.exceptions.InvalidPasswordException;
import comp3350.reshop.logic.exceptions.InvalidUsernameException;
import comp3350.reshop.objects.User;
import comp3350.reshop.data.UserData;
import comp3350.reshop.application.Service;

public class LoginManager {
    private final UserData userData;

    public LoginManager(){
        this.userData = Service.getUserData();
    }

    public LoginManager(UserData db){
        this.userData = db;
    }

    public void validateLogin(String username, String password) throws InvalidInputException {
        User user = userData.getUser(username);

        if (user == null) {
            throw new InvalidUsernameException("Username does not exist.");
        }

        if (!user.getPassword().equals(password)) {
            throw new InvalidPasswordException("Incorrect password.");
        }
    }

    public User getUser(String username) {
        if (username != null) {
            return userData.getUser(username);
        }
        return null;
    }
}
