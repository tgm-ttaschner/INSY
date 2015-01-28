package db_content;

import java.sql.Connection;
import java.util.*;

public class Table {
	
	private String tablename;
	
	private HashMap<String, Column> columns;
	
	public Table(Connection connection, String tablename)	{
		this.tablename = tablename;
		
		
		
		//TODO Abfrage
	}

	public String getTablename() {
		return tablename;
	}

	public HashMap<String, Column> getColumns() {
		return columns;
	}
}