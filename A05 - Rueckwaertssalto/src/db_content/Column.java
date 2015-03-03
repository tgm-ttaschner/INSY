package db_content;


public class Column {
	
	private String columnname;
	
	private boolean isPrimary;
	private String foreign = null;
	
	public Column(String columnname)	{
		this.columnname = columnname;
	}

	public void setForeign(String reference) {
		foreign = reference;
	}

	public boolean isForeign()	{
		return foreign != null;
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

	public String getForeign() {
		return foreign;
	}

}