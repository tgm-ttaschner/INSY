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

			arguments.put("jdbc mysql connector", "com.mysql.jdbc.Driver");
			arguments.put("jdbc postgresql connector", "org.postgresql.Driver");
			arguments.put("jdbc mysql", "jdbc:mysql://");
			arguments.put("jdbc postgresql", "jdbc:postgresql://");
			arguments.put("hostname", host);
			arguments.put("database", database);
			arguments.put("username", username);
			arguments.put("password", password);
			
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
			error += "Es wurde entweder kein oder " + System.getProperty("user.name") + " als Benutzername angegeben. \n";
		}

		if (password == null)	{
			error += "Es wurde kein Passwort angegeben, die Anmeldung erfolgt ohne Passwort. \n";
		}

		System.err.println(error);
	}
}