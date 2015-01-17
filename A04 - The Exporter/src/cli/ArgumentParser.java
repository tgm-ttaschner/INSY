package cli;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.cli.*;

/**
 * @author Patrick Malik
 * @author Thomas Taschner
 * @version 08.01.2015
 */
public class ArgumentParser {
	
	public static Options options = new Options();
	
	private static HashMap<String, String> arguments;
	
	@SuppressWarnings("static-access")
	public static HashMap<String, String> parseArguments(String[] args) throws IllegalArgumentException {
		
		ArrayList<Option> option = new ArrayList<Option>();
		
		option.add(OptionBuilder.hasArg().isRequired().withDescription("Der Hostname bzw. die IP Adresse des DBMS").withArgName("hostname").create("h"));
		option.add(OptionBuilder.hasArg().isRequired().withDescription("Der Benutzername der beim Verbinden mit dem DBMS verwendet werden soll").withArgName("username").create("u"));
		option.add(OptionBuilder.hasArg().isRequired().withDescription("Das Passwort der beim Verbinden mit dem DBMS verwendet werden soll").withArgName("hostname").create("p"));
		option.add(OptionBuilder.hasArg().isRequired().withDescription("Das Passwort der beim Verbinden mit dem DBMS verwendet werden soll").withArgName("hostname").create("p"));
		
		
		return arguments;

	}

	/**
	 * Static method which prints out a help message onto the console.
	 */
	public static void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("RMI", ArgumentParser.options);
	}
}
