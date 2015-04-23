package run;

import gui.Segelverein_GUI;

import java.net.ConnectException;
import java.sql.SQLException;

import cli.ArgumentParser;
import connection.PostgresConnection;

/**
 * @author Thomas Taschner
 * @version 19.01.2015
 * 
 * Die Hauptklasse, in der vom Benutzer die Argumente eingelesen und geparsed werden.
 * Es erfolgt ein Aufruf der GUI.
 * Abschliessend erfolgt noch ein disconnect von der Datenbank.
 * 
 * Sollte etwas schief laufen, so wird ein entsprechender Hilfetext ausgegeben.
 */
public class Main {
	public static void main(String[] args) {

		try {
			ArgumentParser p = new ArgumentParser(args);

			PostgresConnection c = new PostgresConnection(p.getArguments());
			c.connect();
			c.getTableNames();

			Segelverein_GUI test = new Segelverein_GUI(c);
			test.addComboBox();
			test.addTables();
			test.addButtons();
			
			test.refresh();

		} catch (SQLException e)	{
			System.out.println("Es konnte keine Verbindung zur Datenbank hergestellt werden");
			e.printStackTrace();
			Main.printHelp();
		} catch (ClassNotFoundException e)	{
			System.out.println("Der JDBC Treiber konnte nicht geladen werden");
			e.printStackTrace();
		} catch (ConnectException e)	{
			System.out.println("Es konnte keine Verbindung mit dem Datenbanksocket hergestellt werden, die Verbindung wurde hoechstwahrscheinlich verweigert.");
			e.printStackTrace();
		} catch (NullPointerException e)	{
			System.out.println("Einer der Parameter scheint zu fehlen, vergewissern Sie sich, dass alle Parameter eingegeben wurden.");
			e.printStackTrace();
		}
	}

	/**
	 * Gibt alle Schalter und einen kurzen Hilfetext aus.
	 */
	public static void printHelp()	{
		System.out.println("\nEine oder mehrere Eingaben waren fehlerhaft, ueberpruefen Sie bitte Ihre Eingaben: \n\n"
				+ "-d <arg> Name der Datenbank, mit der eine Verbindung hergestellt werden soll \n"
				+ "-h <arg> Hostname/IP-Adresse des DBMS (optional, standardmaessig: 'localhost') \n"
				+ "-p <arg> Passwort, das zur Authentifizierung verwendet werden soll (standardmaessig: keines) \n"
				+ "-u <arg> Benutzername, der zur Authentifizierung verwendet werden soll (standardmaessig: der Name des aktuellen Nutzers des Hostsystems) \n\n"
				
				+ "Beispiel: java -jar Segelverein.jar -h <host> -u <username> -p <password> -d <database name> bzw. konkrekt: \n"
				+ "java -jar -h localhost -u segel -p segel -d segelverein");
	}
}