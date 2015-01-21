package ssteinkellner.connection;

/**
 * eine klasse, in der username und passwort gespeichert wird.
 * das passwort kann nur noch von klassen im selben package abgefragt werden.
 * @author SSteinkellner
 * @version 2015.01.06
 */
public class UserCache {
	private String user, password;

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	String getPassword() {
		return password;
	}
}
