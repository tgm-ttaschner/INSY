package connection;

import java.sql.*;
import java.util.HashMap;

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

		connection = DriverManager.getConnection(arguments.get("jdbc postgresql") + arguments.get("hostname") + "/" + arguments.get("database"), arguments.get("username"), arguments.get("password"));
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