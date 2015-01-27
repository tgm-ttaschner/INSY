package run;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import ssteinkellner.connection.ConnectionHandler;
import ssteinkellner.connection.UserCache;
import ssteinkellner.output.ConsoleWriter;
//import ssteinkellner.output.DebugWriter;
import ssteinkellner.output.FileWriter;
import ssteinkellner.output.Writer;
import ssteinkellner.tools.DBTools;
import ssteinkellner.tools.ListTools;

/**
 *lasse, die alle zur verbindung notwendigen objekte erzeugt und verknuepft, sowie das logfile erstellt
 * @author SSteinkellner
 * @version 2015.01.21
 */
public class Controller {
	private static String defaultLog = "StartupErrorLog.txt";
	private static Writer output = new FileWriter(defaultLog);
	private static ConnectionHandler connectionhandler;
	private UserCache usercache;
	
	public Controller(Map<String, String> arguments){
		if(arguments.containsKey("output")){
			if(arguments.get("output").equalsIgnoreCase("console")){
				output = new ConsoleWriter();
			}else if(arguments.get("output").matches("(.*).(txt|csv)(.*)")){
				output = new FileWriter(arguments.get("output"));
			}else{
				output = new FileWriter(arguments.get("output")+".txt");
			}
		}else{
			String temp = "No Filename for output! Tunneling output to "+defaultLog;
			output.printError(temp);
			System.err.println(temp);
		}
//		output = new DebugWriter(output);
		
		usercache = new UserCache();
		connectionhandler = new ConnectionHandler(usercache);

		connectionhandler.setHost(arguments.get("hostname"));
		connectionhandler.setDatabase(arguments.get("database"));
		usercache.setUser(arguments.get("username"));
		usercache.setPassword(arguments.get("password"));
		arguments.remove("password");
		/*
		 * falls plugins zukuenftig moeglich waeren, ist es sinnvoll,
		 * das passwort aus den arguments zu entfernen, bevor man
		 * die arguments an das plugin weitergibt (falls es auch welche benoetigt)
		 */
		
		run();
	}
	
	private void run(){
		Connection c = connectionhandler.getConnection();
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("tables","SHOW TABLES;");
		querys.put("fields","SELECT * FROM table_name LIMIT 0;");
		querys.put("meta","show fields from table_name;");	// fields/keys
		
		try {
			Statement stmt = c.createStatement();
			ResultSet res = stmt.executeQuery(querys.get("fields").replace("table_name", "film"));
			
			System.out.println(ListTools.listToString(DBTools.getTableHead(res)));
			
			res.close();
			stmt.close();
		} catch (SQLException e) {
			output.printException(e);
		}
		
		connectionhandler.close();
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
