package db_content;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import run.Controller;

public class Table {
	
	private String tableName;
	private static String  query = "SELECT COLUMN_NAME, COLUMN_KEY FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = ?";
	
	private Map<String, Column> columns;
	
	public Table(Connection connection, String tableName)	{
		this.tableName = tableName;
		
		columns = new HashMap<String, Column>();
		
		collectTableData(connection);
	}

	public String getTablename() {
		return tableName;
	}

	public Map<String, Column> getColumns() {
		return columns;
	}
	
	private void collectTableData(Connection connection){
		try(
			PreparedStatement pstmt = connection.prepareStatement(query);
		) {
			pstmt.setString(1, tableName);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				String name = rs.getString("COLUMN_NAME");
				Column spalte = new Column(name);
				if(rs.getString("COLUMN_KEY").equalsIgnoreCase("PRI")){
					spalte.setPrimary(true);
				}
				
//				Controller.getOutput().printLine(spalte.getColumnname() + " ist " +((spalte.isPrimary())?"":"k")+"ein Primary Key.");	//debug
				
				columns.put(name, spalte);
			}
			
			rs.close();
		} catch (SQLException e) {
			Controller.getOutput().printException(e);
		}
	}
}