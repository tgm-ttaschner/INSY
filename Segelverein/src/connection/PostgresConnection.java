package connection;

import java.sql.*;
import java.util.HashMap;

import org.postgresql.jdbc4.Jdbc4Connection;

/**
 * @author Thomas Taschner
 * @version 19.01.2015
 * 
 * Diese Klasse verbindet sich mit der Datenbank, fuehrt die Abfrage aus und das ResultSet wird gespeichert.
 * Eine disconnect Methode ermoeglicht ein Schliessen aller verwendeten Ressourcen.
 * Fuer jedes Attribut gibt es auch eine Getter Methode.
 * 
 * Sollte keine Verbindung hergestellt werden können, so wird eine SQLException geworfen.
 */
public class PostgresConnection {

	private String query;

	private String output;

	private String seperator;

	private int columncount;

	private Statement statement;

	private Connection connection;

	private ResultSet resultSet;

	private HashMap<String, String> arguments;

	/**
	 * @param arguments die Argumente, die ueber die Konsole in Form einer Map mitgegeben werden
	 * @throws SQLException wird geworfen, wenn keine Verbindung mit der Datenbank hergestellt werden kann
	 * 
	 * Konstruktor der Klasse
	 */
	public PostgresConnection(HashMap<String, String> arguments) throws SQLException	{
		this.arguments = arguments;
	}

	/**
	 * @return die aus der Konsole ausgelesene Abfrage
	 * 
	 * Getter fuer query.
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @return die in der Konsole eingegebene Outputmethode
	 * 
	 * Getter fuer output.
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * @return das in der Konsole eingegebene Trennzeichen
	 * 
	 * Getter fuer seperator.
	 */
	public String getSeperator() {
		return seperator;
	}

	/**
	 * @return die Spaltenanzahl des Resultsets
	 * 
	 * Getter fuer columncount.
	 */
	public int getColumncount() {
		return columncount;
	}

	/**
	 * @return das SQL Statemanet
	 * 
	 * Getter fuer statement.
	 */
	public Statement getStatement() {
		return statement;
	}

	/**
	 * @return die Verbindung zur Datenbank
	 * 
	 * Getter fuer connection.
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * @return das ResultSet der Abfrage der Verbindung
	 * @throws SQLException wird geworfen, wenn keine Verbindung zur Datenbank hergestellt werden kann
	 * 
	 * Getter fuer ResultSet.
	 */
	public ResultSet getResultSet() throws SQLException {
		return resultSet;
	}

	/**
	 * @return die in der Konsole eingegebenen Argumente, die fuer die Verbindung, Abfrage und Ausgabe benoetigt
	 * 
	 * Getter fuer arguments.
	 */
	public HashMap<String, String> getArgs() {
		return arguments;
	}

	/**
	 * @throws SQLException wird geworfen, wenn keine Verbindung zur Datenbank hergestellt werden kann
	 * @throws ClassNotFoundException wird geworfen, wenn der JDBC MySQL Treiber nicht geladen werden kann
	 * 
	 * Laedt den JDBC MySQL Treiber und verbindet sich mit der Datenbank.
	 * Eine entsprechende Exception wird geworfen, sollte etwas schief gehen.
	 */
	public void connect() throws SQLException, ClassNotFoundException {

		Class.forName(arguments.get("jdbc postgresql connector"));

		connection = (Jdbc4Connection) DriverManager.getConnection(arguments.get("jdbc postgresql") + arguments.get("hostname") + "/" + arguments.get("database"), arguments.get("username"), arguments.get("password"));
	}

	/**
	 * @throws SQLException wird geworfen, wenn keine Verbindung zur Datenbank hergestellt werden kann
	 * 
	 * Die Abfrage wird hier zusammengesetzt, ein statement gebildet und an die Datenbank geschickt.
	 * Das Ergebnis wird in einem ResultSet gespeichert.
	 * Abschliessend erfolgt noch ein Check, wie viele Spalten im Resultset enthalten sind.
	 * 
	 * Sollte keine Verbindung zur Datenbank hergestellt werden können, so wird eine SQLException geworfen.
	 */

	public void query(String rows, String table, String condition, String order_condition, String sortdir) throws SQLException	{
		query = "SELECT ";

		query += rows + " FROM ";

		query += table + " ";

		if (condition != null)	{
			query += "WHERE " + condition + " ";
		}

		if (order_condition != null)	{
			query += "ORDER BY " + order_condition + " ";

			if (sortdir != null)	{
				query += sortdir;
			}
		}

		query += ";";

		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		resultSet = statement.executeQuery(query);

		if (rows.equals("*"))	{
			columncount = resultSet.getMetaData().getColumnCount();
		} else {
			columncount = rows.split(",").length;
		}
	}
	
	/**
	 * @throws SQLException wird geworfen, wenn keine Verbindung zur Datenbank hergestellt werden kann
	 * 
	 * Die Abfrage wird hier zusammengesetzt, ein statement gebildet und an die Datenbank geschickt.
	 * Das Ergebnis wird in einem ResultSet gespeichert.
	 * Abschliessend erfolgt noch ein Check, wie viele Spalten im Resultset enthalten sind.
	 * 
	 * Sollte keine Verbindung zur Datenbank hergestellt werden können, so wird eine SQLException geworfen.
	 */
	public void query(String query) throws SQLException	{
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		statement.executeUpdate(query);

		if (arguments.get("rows").equals("*"))	{
			columncount = resultSet.getMetaData().getColumnCount();
		} else {
			columncount = arguments.get("rows").split(",").length;
		}
	}
	
	/**
	 * @throws SQLException wird geworfen, wenn keine Verbindung zur Datenbank hergestellt werden kann
	 * 
	 * Die Abfrage wird hier zusammengesetzt, ein statement gebildet und an die Datenbank geschickt.
	 * Das Ergebnis wird in einem ResultSet gespeichert.
	 * Abschliessend erfolgt noch ein Check, wie viele Spalten im Resultset enthalten sind.
	 * 
	 * Sollte keine Verbindung zur Datenbank hergestellt werden können, so wird eine SQLException geworfen.
	 */
	public int getResultSetSize(ResultSet rs) throws SQLException	{

		int size = 0;

		while (rs.next())	{
			size++;
		}

		rs.beforeFirst();

		return size;
	}

	public String[] getColumnNames(String tablename) throws SQLException	{

		ResultSetMetaData rsmd = resultSet.getMetaData();

		String[] columns;


		columns = new String[rsmd.getColumnCount()];

		for (int i = 1; i <= rsmd.getColumnCount(); i++)	{
			columns[i-1] = rsmd.getColumnName(i);
		}

		return columns;

	}

	/**
	 * @throws SQLException wird geworfen, wenn keine Verbindung zur Datenbank hergestellt werden kann
	 * 
	 * Schliesst alle vorhin benoetigten Ressourcen.
	 * 
	 * Sollte keine Verbindung zur Datenbank hergestellt werden können, so wird eine SQLException geworfen.
	 */
	public void disconnect() throws SQLException {	
		resultSet.close();
		statement.close();
		connection.close();
	}
}