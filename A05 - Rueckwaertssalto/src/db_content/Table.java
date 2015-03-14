package db_content;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ssteinkellner.connection.ConnectionHandler;

public class Table {
	private String tableName;
	private ConnectionHandler ch;
	
	public Table(ConnectionHandler connectionHandler, String tableName)	{
		this.tableName = tableName;
		ch = connectionHandler;
	}

	public String getTablename() {
		return tableName;
	}

	public Map<String, Column> getColumns() throws SQLException {
		HashMap<String, Column> columns = new HashMap<>();
		ResultSet rs = ch.getConnection().getMetaData().getColumns(null, ch.getDatabase(), this.tableName, null);
		
		ArrayList<String> primarys = getPrimaryKeys();
		HashMap<String, String> foreigns = getForeignKeys();
		
		Column temp;
		while (rs.next()) {
			temp = new Column(rs.getString("COLUMN_NAME"));
/*			if(rs.getString("COLUMN_KEY").equalsIgnoreCase("PRI")){
				temp.setPrimary(true);
			}
			
			if(rs.getString("REFERENCED_TABLE_NAME")!=null){
				temp.addForeign(rs.getString("REFERENCED_TABLE_NAME")+"."+rs.getString("REFERENCED_COLUMN_NAME"));
			}*/

			String key = this.tableName + "." + rs.getString("COLUMN_NAME");
			
			if(primarys.contains(key)) temp.setPrimary(true);
			if(foreigns.containsKey(key)) temp.setForeign(foreigns.get(key));
			
//			columns.put(this.tableName + "." + rs.getString("COLUMN_NAME"), temp);
			columns.put(rs.getString("COLUMN_NAME"), temp);
		}

		return columns;
	}
	
	public ArrayList<String> getPrimaryKeys() throws SQLException	{
		ArrayList<String> primary = new ArrayList<String>();
		ResultSet rs = ch.getConnection().getMetaData().getPrimaryKeys(null, ch.getDatabase(), this.tableName);

		while (rs.next())	{
			primary.add(this.tableName + "." + rs.getString("COLUMN_NAME"));
		}

		return primary;
	}

	public HashMap<String, String> getForeignKeys() throws SQLException	{
		HashMap<String, String> foreigns = new HashMap<String, String>();
		ResultSet rs = ch.getConnection().getMetaData().getImportedKeys(null, ch.getDatabase(), this.tableName);

		while (rs.next())	{
			foreigns.put(rs.getString("FKTABLE_NAME") + "." + rs.getString("FKCOLUMN_NAME"), rs.getString("PKTABLE_NAME") + "." + rs.getString("PKCOLUMN_NAME"));
		}

		return foreigns;
	}
	
	public ArrayList<String> getForeignKeysAsList() throws SQLException	{
		ArrayList<String> foreigns_list = new ArrayList<String>();
		ResultSet rs = ch.getConnection().getMetaData().getImportedKeys(null, ch.getDatabase(), this.tableName);
		
		while (rs.next())	{
			foreigns_list.add(rs.getString("FKTABLE_NAME") + "." + rs.getString("FKCOLUMN_NAME") + "=" + rs.getString("PKTABLE_NAME") + "." + rs.getString("PKCOLUMN_NAME"));
		}
		
		return foreigns_list;
		
	}
	
	public ArrayList<String> getTableRelations() throws SQLException	{
		ArrayList<String> rel = new ArrayList<String>();
		ResultSet rs = ch.getConnection().getMetaData().getImportedKeys(null, ch.getDatabase(), this.tableName);
		
		while (rs.next())	{
			rel.add(rs.getString("FKTABLE_NAME") + "=" + rs.getString("PKTABLE_NAME"));
		}
		
		return rel;
		
	}
}