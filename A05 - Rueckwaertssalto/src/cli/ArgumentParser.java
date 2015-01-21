package cli;

import java.util.HashMap;

import org.kohsuke.args4j.*;

/**
 * @author Thomas Taschner
 * @version 19.01.2015
 * 
 * Der Parser analysiert den Konsoleninput, setzt eventuell Standardwerte und gibt bei fehlerhaften Eingaben eine entsprechende Fehlermeldung aus.
 */
public class ArgumentParser {

	private HashMap<String, String> arguments;

	@Option(name="-h")
	private String host = "localhost";

	@Option(name="-u")
	private String username = System.getProperty("user.name");

	@Option(name="-p")
	private String password;

	@Option(name="-d", required = true)
	private String database;

	@Option(name="-s")
	private String sortby;

	@Option(name="-r", depends = {"-s"})
	private String sortdir = "ASC";

	@Option(name="-w")
	private String where_condition;

	@Option(name="-t")
	private String seperator = ";";

	@Option(name="-f", required = true)
	private String rows;

	@Option(name="-o")
	private String output = "console";

	@Option(name="-T", required = true)
	private String tablename;

	/**
	 * @param args main Arguments which will be parsed
	 * 
	 * Die Argumente werden geparsed und in einer HashMap zusammen mit einer aussagekraeftigen Bezeichnung gespeichert.
	 * Sollte etwas schief laufen, dann wird die Hilfe ausgegeben.
	 */
	public ArgumentParser(String[] args)	{
		arguments = new HashMap<String, String>();
		
		try {
			CmdLineParser cmdLineParser = new CmdLineParser(this);

			cmdLineParser.parseArgument(args);

			arguments.put("jdbc", "com.mysql.jdbc.Driver");
			arguments.put("jdbc mysql", "jdbc:mysql://");
			arguments.put("hostname", host);
			arguments.put("database", database);
			arguments.put("username", username);
			arguments.put("password", password);
			arguments.put("rows", rows);
			arguments.put("table", tablename);
			arguments.put("where", where_condition);
			arguments.put("sort", sortby);
			arguments.put("sortdir", sortdir);
			arguments.put("seperator", seperator);
			arguments.put("output", output);

		} catch (CmdLineException e) {
			printHelp();
		}
	}

	/**
	 * @return arguments the parsers arguments
	 * 
	 * Returns the parsers arguments.
	 */
	public HashMap<String, String> getArguments()	{
		return arguments;
	}

	/**
	 * Gibt einen Hilfetext aus.
	 * Wenn ein Argument (+ Schalter) fehlt oder einfach nur ein Argument fehlerhaft ist (falscher Host, Benutzer, ...) wird eine entsprechende Fehlermeldung ausgegeben.
	 */
	public void printHelp()	{
		
		String error = "";

		if (host.equals("localhost"))	{
			error += "Es wurde localhost als Host oder gar kein Host Parameter angegeben. \n";
		}

		if (database == null)	{
			error += "Es wurde kein Datenbankname angegeben. \n";
		}

		if (username.equals(System.getProperty("user.name")))	{
			error += "Es wurde " + System.getProperty("user.name") + " als Benutzername oder gar kein Benutzname angegeben. \n";
		}

		if (password == null)	{
			error += "Es wurde kein Passwort angegeben, zur Anmeldung wird kein Passwort verwendet. \n";
		}

		if (rows == null)	{
			error += "Es wurde keine anzuzeigende Spalte angegeben, geben Sie * ein, um alle Spalten ausgeben zu lassen. \n";
		}

		if (tablename == null)	{
			error += "Es wurde keine Tabelle angegeben. \n";
		}

		if (where_condition == null)	{
			error += "Es wurde keine WHERE Bedingung angegeben. \n";
		}

		if (sortby == null)	{
			error += "Es wurde keine Spalte angegeben nach der sortiert werden soll. \n";
		}

		if (sortdir == "ASC")	{
			error += "Es wurde die aufsteigende oder gar keine Sortierrichtung angegeben, es wird aufsteigend sortiert. \n";
		}

		if (seperator == null)	{
			error += "Es wurde kein Trennzeichen angegeben, es wird ';' als Trennzeichen verwendet. \n";
		}

		if (output == "output")	{
			error += "Es wurde die Konsole aös Ausgabemethode definiert oder gar keine, die Ausgabe erfolgt auf der Konsole. \n";
		}

		System.err.println(error);
	}
}