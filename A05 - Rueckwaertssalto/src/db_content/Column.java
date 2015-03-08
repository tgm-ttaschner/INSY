package db_content;


public class Column {
	
	private String columnName;
	
	private boolean isPrimary;
	private String foreign = null;
	
	public Column(String columnName)	{
		this.columnName = columnName;
	}

	public void setForeign(String reference) {
		foreign = reference;
	}

	public boolean isForeign()	{
		return foreign != null;
	}
	
	public String getColumnName() {
		return columnName;
	}

	public boolean isPrimary() {
		return isPrimary;
	}

	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public String getForeign() {
		return foreign;
	}

}