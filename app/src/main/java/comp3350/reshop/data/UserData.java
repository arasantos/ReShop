package comp3350.reshop.data;

import comp3350.reshop.objects.User;

public interface UserData {
	void insertUser(User currentUser);

	User getUser(String username);

	void updateUser(String username, User user);
}
