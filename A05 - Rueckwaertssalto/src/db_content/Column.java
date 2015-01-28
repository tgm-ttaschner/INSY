package db_content;

import java.util.*;

public class Column {
	
	private String columnname;
	
	private boolean isPrimary;
	
	private List<String> foreigns;
	
	public Column(String columnname)	{
		foreigns = new ArrayList<String>();
		this.columnname = columnname;
	}

	public void addForeign(String reference) {
		foreigns.add(reference);
	}

	public boolean isForeign()	{
		return (foreigns.size() > 0);
	}
	
	public String getColumnname() {
		return columnname;
	}

	public boolean isPrimary() {
		return isPrimary;
	}

	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public List<String> getForeigns() {
		return foreigns;
	}

}