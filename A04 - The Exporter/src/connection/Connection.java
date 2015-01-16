package connection;

import output.format.ResultSet;

public abstract class Connection {

	private Connection conn;

	private ResultSet resultSet;

	public Connection(String[] arguments) {

	}

	public ResultSet getResultSet() {
		return null;
	}

	public abstract void connect(Connection con);

}
