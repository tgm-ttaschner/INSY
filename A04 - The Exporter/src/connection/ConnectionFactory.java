package connection;

public class ConnectionFactory {

	public ConnectionFactory(String type) {
		switch (type) {
		case "MySQL":
			new MySQLConnection(null);
			break;
		case "":
		default:
			break;
		}
	}

}
