package connection;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class MySQLConnection {

	private String query;

	private String output;

	private String seperator;

	private int columncount;

	private Statement statement;

	private Connection connection;

	private ResultSet resultSet;

	private HashMap<String, String> args;

	public MySQLConnection(HashMap<String, String> arguments) throws SQLException	{
		this.args = arguments;
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

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public HashMap<String, String> getArgs() {
		return args;
	}

	public void setArgs(HashMap<String, String> args) {
		this.args = args;
	}

	public void connect() throws SQLException, ClassNotFoundException {

		Class.forName(args.get("jdbc"));

		connection = DriverManager.getConnection(args.get("jdbc mysql") + args.get("hostname") + "/" + args.get("database"), args.get("username"), args.get("password"));
	}

	public void query() throws SQLException	{
		query = "SELECT ";

		query += args.get("rows") + " FROM ";

		query += args.get("table") + " ";

		if (args.containsKey("where") && args.get("where") != null)	{
			query += "WHERE " + args.get("where") + " ";
		}

		if (args.containsKey("sort") && args.get("sort") != null)	{
			query += "ORDER BY " + args.get("sort") + " ";

			if (args.containsKey("sortdir") && args.get("sortdir") != null)	{
				query += args.get("sortdir") + " ";
			}
		}

		query += ";";
		
		statement = connection.createStatement();
		resultSet = statement.executeQuery(query);

		if (args.get("rows").equals("*"))	{
			columncount = resultSet.getMetaData().getColumnCount();
		} else {
			columncount = args.get("rows").split(",").length;
		}

		seperator = args.get("seperator");
		output = args.get("output");
	}

	public void disconnect() throws SQLException {
		connection.close();
		statement.close();
		resultSet.close();
	}
}