package run;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ssteinkellner.connection.ConnectionHandler;
import ssteinkellner.connection.UserCache;
import ssteinkellner.output.ConsoleWriter;
//import ssteinkellner.output.DebugWriter;
import ssteinkellner.output.FileWriter;
import ssteinkellner.output.Writer;
import db_content.Column;
import db_content.Table;

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
	
	private Map<String, Table> tables;
	
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
		
		tables = new HashMap<>();
		
		run();
	}

	private void run(){
		try {
			run_connection();
			run_rm();
			run_dot();
		} catch (SQLException e) {
			output.printException(e);
		}
		
		connectionhandler.close();
	}

	private void run_connection() throws SQLException{
		/*
		 * um so wenige gleichzeitige verbindungen wie möglich zu haben,
		 * werden die tabellennamen zuerst in eine liste geschrieben und erst nacher ausgelesen
		 */
		List<String> tableNames = new LinkedList<String>();
		
		Connection c = connectionhandler.getConnection();
		
		ResultSet rs = c.getMetaData().getTables(null, connectionhandler.getDatabase(), null, null);
		while(rs.next()){
			tableNames.add(rs.getString("TABLE_NAME"));
		}
		rs.close();
		
		for(String tableName : tableNames){
			tables.put(tableName, new Table(connectionhandler, tableName));
		}
	}
	
	private void run_rm() throws SQLException {
		Writer html = new FileWriter("rm.html", true);
		html.printLine("<html><body>");
		
		Table table;
		Column column;
		String temp_line;
		for(String tableName : tables.keySet()){
			table = tables.get(tableName);
			temp_line = "";
			for(String ColumnName : table.getColumns().keySet()){
				column = table.getColumns().get(ColumnName);
				temp_line += ((!temp_line.isEmpty())?", ":"") + columnToString(column);
			}
			html.printLine(tableName+"("+temp_line+")");
			html.printLine("<br />");
		}

		html.printLine("</body></html>");
	}
	
	/**
	 * this method generates an html string that represents the column
	 * @param column column that should be represented
	 * @return column formatted as html
	 */
	private String columnToString(Column column){
		String text;
		if(column.isForeign()){
			text = column.getForeign();
			
			if(!column.getForeign().split("\\.")[1].equals(column.getColumnName())){
				text += ":"+column.getColumnName();
			}
			
			text = "<i>"+text+"</i>";
		}else{
			text = column.getColumnName();
		}
		
		if(column.isPrimary()){ text = "<u>"+text+"</u>"; }
		
		return text;
	}
	

	private void run_dot() throws SQLException	{
		Table entity;
		Column attribute;

		String database_name = connectionhandler.getDatabase().toString();
		
		Writer dot = new FileWriter(database_name + ".dot", true);
		dot.printLine("graph " + database_name + " {");
		dot.printLine("");
		dot.printLine("");
		
		for (int i = 0; i < tables.keySet().size(); i++)	{
			
			String entity_name = (String) tables.keySet().toArray()[i];
			entity = tables.get(entity_name);
			
			dot.printLine(entity_name + "[shape=box];");
			
			for (int j = 0; j < entity.getColumns().keySet().size(); j++)	{
				String attibute_name = (String) entity.getColumns().keySet().toArray()[j];
				
				attribute = entity.getColumns().get(attibute_name);
				
				dot.printLine(entity_name + " -- " + attibute_name + ";");
				
				if (attribute.isPrimary())	{
					dot.printLine(attribute.getColumnName() + "[shape=ellipse, style=filled, color=lightgrey]");
				}
				
				dot.printLine("");
				
				dot.printLine("abc_def[shape=diamond, label=\"\"]");
				
			}
			dot.printLine("");
		}
		
		dot.printLine("label = \"" + database_name + "\";");
		
		dot.printLine("}");
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
