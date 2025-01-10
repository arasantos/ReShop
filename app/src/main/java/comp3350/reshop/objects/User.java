package comp3350.reshop.objects;

public class User {

	private final String username;
	private final String password;
	private final String firstName;
	private final String lastName;
	private final String city;

	public User(final String username, final String password, final String firstName,
				final String lastName, final String city) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.city = city;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return firstName + " " + lastName;
	}

	public String getFirstName() { return firstName; }

	public String getLastName() { return lastName; }

	public String getCity() {
		return city;
	}
}
