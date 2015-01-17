package connection;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.mysql.jdbc.PreparedStatement;

public class MySQLConnection {
	
	private Statement statement;

	private Connection connection;

	private ResultSet resultSet;
	
	private HashMap<String, String> args;

	public MySQLConnection(HashMap<String, String> arguments) throws ClassNotFoundException, SQLException {
		this.setArgs(arguments);
		Class.forName(arguments.get("jdbc"));
		
		connection = DriverManager.getConnection(arguments.get("jdbc mysql") + arguments.get("hostname") + "/" + arguments.get("database"), arguments.get("username"), arguments.get("password"));
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * @return the args
	 */
	public HashMap<String, String> getArgs() {
		return args;
	}

	/**
	 * @param args the args to set
	 */
	public void setArgs(HashMap<String, String> args) {
		this.args = args;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	/**
	 * @return the statement
	 */
	public Statement getStatement() {
		return statement;
	}

	/**
	 * @param statement the statement to set
	 */
	public void setStatement(PreparedStatement statement) {
		this.statement = statement;
	}

	public ResultSet connect() throws SQLException {
		statement = connection.createStatement();
		
		resultSet = statement.executeQuery("SELECT * FROM film order by titel;");
		
		return resultSet;
	}
	
	public void disconnect() throws SQLException {
		connection.close();
		statement.close();
		resultSet.close();
	}
}