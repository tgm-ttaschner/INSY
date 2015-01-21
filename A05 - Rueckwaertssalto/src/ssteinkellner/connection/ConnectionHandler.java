package ssteinkellner.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import ssteinkellner.Controller;

/**
 * eine klasse, die fuer das auf/abbauen von verbindungen zustaendig ist.
 * es gibt maximal eine connection.
 * @author SSteinkellner
 * @version 2015.01.21
 */
public class ConnectionHandler {
	private Connection connection;
	private UserCache usercache;
	private String host, database;
	
	/**
	 * erstellt einen neuen connectionhandler mit dem erg&auml;nzenden usercache,
	 * der f&uuml;rs speichern von username und passwort verwendet wird
	 * @param userCache
	 */
	public ConnectionHandler(UserCache userCache){
		usercache = userCache;
	}

	/**
	 * setzt den host f&uuml;r die verbingung.
	 * <br><b>aber nur</b> wenn, noch keine verbindung ge&ouml;ffnet wurde
	 * (mit {@link #getConnection() getConnection})
	 * @param host datenbankname
	 */
	public void setHost(String host) {
		if(connection==null){
			this.host = host;
		}
	}

	public String getHost() {
		return host;
	}

	/**
	 * setzt die datenbank, auf die zugegriffen werden soll.
	 * <br><b>aber nur</b> wenn, noch keine verbindung ge&ouml;ffnet wurde
	 * (mit {@link #getConnection() getConnection})
	 * @param database datenbankname
	 */
	public void setDatabase(String database) {
		if(connection==null){
			this.database = database;
		}
	}

	public String getDatabase() {
		return database;
	}
	
	/**
	 * erstellt beim ersten aufruf eine connection.
	 * <br>gibt immer eine connection zurueck.
	 * @return connection
	 */
	public Connection getConnection() {
		if(connection==null){
			connStart();
		}
		
		return connection;
	}
	

	/**
	 * loads the database driver (com.mysql.jdbc.Driver) and connects to the database.
	 * closes the programm, if there is an error
	 */
	private void connStart() {

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch(Exception ex) {
			Controller.getOutput().printError("Can't find Database driver class!");
			Controller.exit(1);
		}

		try {
			connection = (Connection) DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, usercache.getUser(), usercache.getPassword());
			Controller.getOutput().printLine("Successfully connected to " + database + " on " + host);
		} catch(SQLException ex) {
			Controller.getOutput().printError("DB connection error!");
			Controller.exit(1);
		}
	}
	
	/**
	 * schlieﬂt die connection, falls eine connection erstllt wurde
	 * (mit {@link #getConnection() getConnection})
	 */
	public void close(){
		if(connection != null){
			try {
				if(!connection.isClosed()){
					connection.close();
				}
			} catch (SQLException e) {
				Controller.getOutput().printError("Couldn't close Connection!");
			}
		}
	}
}
