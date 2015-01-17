package run;

import java.sql.SQLException;
import java.util.HashMap;

import connection.MySQLConnection;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		HashMap<String, String> arguments = new HashMap<String, String>();
		arguments.put("jdbc", "com.mysql.jdbc.Driver");
		arguments.put("jdbc mysql", "jdbc:mysql://");
		arguments.put("hostname", "localhost");
		arguments.put("database", "premiere");
		arguments.put("username", "insy4");
		arguments.put("password", "blabla");
		
		MySQLConnection c = new MySQLConnection(arguments);
		
		c.connect();
	}
}