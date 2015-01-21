package ssteinkellner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import ssteinkellner.connection.ConnectionHandler;
import ssteinkellner.connection.InjectionSafetyException;
import ssteinkellner.connection.UserCache;
import ssteinkellner.output.ConsoleWriter;
import ssteinkellner.output.FileWriter;
import ssteinkellner.output.Writer;
import ssteinkellner.tools.ArrayTools;
import ssteinkellner.tools.DBTools;
import ssteinkellner.tools.ListTools;

/**
 * hauptklasse, die alle objekte erzeugt und verknuepft
 * @author SSteinkellner
 * @version 2014.12.30
 */
public class Controller {
	private static Writer output, content;
	private static ConnectionHandler connectionhandler;
	private UserCache usercache;
	
	public Controller(){
		content = new FileWriter("dbcontent.txt");
//		output = new FileWriter("log.txt");
		output = new ConsoleWriter();
		usercache = new UserCache();
		connectionhandler = new ConnectionHandler(usercache);

		connectionhandler.setHost("localhost");
		connectionhandler.setDatabase("premiere");
		usercache.setUser("insy4");
		usercache.setPassword("blabla");
	}
	
	/**
	 * if an output is set, this will be returned. otherwise null
	 * @return output or null
	 */
	public static Writer getOutput(){
		return output;
	}
	
	/**
	 * closes the connection and exits the program
	 * @param error errornumber or 0 if correct exit
	 */
	public static void exit(int error){
		connectionhandler.close();
		System.exit(error);
	}
	
	/**
	 * prints a text to console and then calls {@link #exit() exit}.
	 * @param text text to be printed
	 * @param error errornumber or 0 if correct exit
	 */
	public static void exit(String text, int error){
		System.out.println(text);
		exit(error);
	}
}
