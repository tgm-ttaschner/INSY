package output.format;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class Process {

	private ArrayList<String> formattedOutput;

	private String seperator;

	private ResultSet resultSet;

	private ResultSetMetaData rsmd;

	public Process(ResultSet resultSet, String seperator) throws SQLException {
		this.resultSet = resultSet;
		this.seperator = seperator;

		formattedOutput = new ArrayList<String>();

		rsmd = resultSet.getMetaData();
	}

	public ArrayList<String> readAll() throws SQLException {
		while (resultSet.next())	{

			String column = "";

			for (int i = 1; i <= rsmd.getColumnCount(); i++)	{
				if (i == rsmd.getColumnCount())	{
					column += resultSet.getObject(i);
				} else {
					column += resultSet.getObject(i) + seperator + " ";
				}
			}

			formattedOutput.add(column);
		}

		return formattedOutput;
	}
}