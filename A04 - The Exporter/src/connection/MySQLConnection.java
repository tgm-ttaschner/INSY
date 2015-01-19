package connection;

import java.sql.*;
import java.util.HashMap;

public class MySQLConnection {

	private String query;

	private String output;

	private String seperator;

	private int columncount;

	private Statement statement;

	private Connection connection;

	private ResultSet resultSet;

	private HashMap<String, String> arguments;

	public MySQLConnection(HashMap<String, String> arguments) throws SQLException	{
		this.arguments = arguments;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getOutput() throws IllegalArgumentException {
		if (output != null)	{
			return output;
		} else {
			throw new IllegalArgumentException("Ungueltiger Ausgabetyp!");
		}
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getSeperator() {
		return seperator;
	}

	public void setSeperator(String seperator) {
		this.seperator = seperator;
	}

	public int getColumncount() {
		return columncount;
	}

	public void setColumncount(int columncount) {
		this.columncount = columncount;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public ResultSet getResultSet() throws SQLException {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public HashMap<String, String> getArgs() {
		return arguments;
	}

	public void setArgs(HashMap<String, String> args) {
		this.arguments = args;
	}

	public void connect() throws SQLException, ClassNotFoundException {

		Class.forName(arguments.get("jdbc"));

		connection = DriverManager.getConnection(arguments.get("jdbc mysql") + arguments.get("hostname") + "/" + arguments.get("database"), arguments.get("username"), arguments.get("password"));
	}

	public void query() throws SQLException	{
		query = "SELECT ";

		query += arguments.get("rows") + " FROM ";

		query += arguments.get("table") + " ";

		if (arguments.containsKey("where") && arguments.get("where") != null)	{
			query += "WHERE " + arguments.get("where") + " ";
		}

		if (arguments.containsKey("sort") && arguments.get("sort") != null)	{
			query += "ORDER BY " + arguments.get("sort") + " ";

			if (arguments.containsKey("sortdir") && arguments.get("sortdir") != null)	{
				query += arguments.get("sortdir");
			}
		}

		query += ";";
		
		statement = connection.createStatement();
		resultSet = statement.executeQuery(query);

		if (arguments.get("rows").equals("*"))	{
			columncount = resultSet.getMetaData().getColumnCount();
		} else {
			columncount = arguments.get("rows").split(",").length;
		}

		seperator = arguments.get("seperator");
		output = arguments.get("output");
	}

	public void disconnect() throws SQLException {
		connection.close();
		statement.close();
		resultSet.close();
	}
}