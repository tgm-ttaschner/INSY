package run;

import gui.Segelverein_GUI;

import java.sql.SQLException;
import java.util.*;

import cli.ArgumentParser;
import connection.PostgresConnection;

/**
 * @author Thomas Taschner
 * @version 19.01.2015
 * 
 * Die Hauptklasse, in der vom Benutzer die Argumente eingelesen und geparsed werden.
 * Abschliessend wird eine Verbindung zur Datenbank aufgebaut und die Abfrage durchgefuehrt.
 * Nun wird das Ergebnis dieser Abfrage (Resultset) verarbeitet und entsprechend ausgegeben (bzw. geschrieben).
 * Abschliessend erfolgt noch ein disconnect von der Datenbank.
 * 
 * Sollte etwas schief laufen, so wird ein entsprechender Hilfetext ausgegeben.
 */
public class Main {
	public static void main(String[] args) {

		try {
			
			String rows = "*";
			
			/*
			String host = JOptionPane.showInputDialog("Geben Sie bitte den Host an");
			String database = JOptionPane.showInputDialog("Geben Sie bitte den Datenbanknamen an");
			String username = JOptionPane.showInputDialog("Geben Sie bitte den Benutzernamen an");
			String password = JOptionPane.showInputDialog("Geben Sie bitte das Passwort ein");
			
			*/
			
			/*
			String host = "localhost";
			String database = "muster";
			String username = "segel";
			String password = "segel";
			
			HashMap<String, String> arguments = new HashMap<String, String>();

			arguments = new HashMap<>();
			arguments.put("jdbc postgresql connector", "org.postgresql.Driver");
			arguments.put("jdbc postgresql", "jdbc:postgresql://");
			arguments.put("hostname", host);
			arguments.put("database", database);
			arguments.put("username", username);
			arguments.put("password", password);
			//arguments.put("rows", rows);
			*/
			ArgumentParser p = new ArgumentParser(args);

			PostgresConnection c = new PostgresConnection(p.getArguments());
			c.connect();

			Segelverein_GUI test = new Segelverein_GUI(c);
			test.addComboBox();
			test.addTables();
			test.addButtons();
			
			test.refresh();
			
			//c.disconnect();

		} catch (SQLException e)	{
			System.out.println("Es konnte keine Verbindung zur Datenbank hergestellt werden");
			e.printStackTrace();
			Main.printHelp();
		} catch (ClassNotFoundException e)	{
			System.out.println("Der JDBC Treiber konnte nicht geladen werden");
		} catch (Exception ex)	{

		}
	}

	/**
	 * Gibt alle Schalter und einen kurzen Hilfetext aus.
	 */
	public static void printHelp()	{
		System.out.println("\nEine oder mehrere Eingaben waren fehlerhaft, ueberpruefen Sie bitte Ihre Eingaben: \n\n"
				+ "-d <arg> Name der Datenbank, mit der eine Verbindung hergestellt werden soll \n"
				+ "-f <arg> Kommagetrennte Liste (ohne Leerzeichen) der Felder, die im Ergebnis enthalten sein sollen \n"
				+ "-h <arg> Hostname/IP-Adresse des DBMS (optional, standardmaessig: 'localhost') \n"
				+ "-o <arg> Ausgabetyp (optional, standardmaessig: 'console', verfuegbar: 'console', 'file') \n"
				+ "-p <arg> Passwort, das zur Authentifizierung verwendet werden soll (standardmaessig: keines) \n"
				+ "-r <arg> Sortierrichtung (optional, standardmaessig: 'ASC', verfuegbar: 'ASC', 'DESC') \n"
				+ "-s <arg> Feld, nach dem sortiert werden soll (optional) \n"
				+ "-T <arg> Tabellenname \n-t <arg> Trennzeichen, dass für die Ausgabe verwendet werden soll \n"
				+ "-u <arg> Benutzername, der zur Authentifizierung verwendet werden soll (standardmaessig: der Name des aktuellen Nutzers des Hostsystems) \n"
				+ "-w <arg> eine Bedingung, in SQL-Syntax, die um Filtern der Tabelle verwendet wird \n\n"
				+ "Beispiel: java -jar TheExporter.jar -h projekte.tgm.ac.at -u test -p test -d megamarkt -T produkt -f titel,preis -s preis -r DESC -w \"preis > 4\" -t \"$\"");
	}
}