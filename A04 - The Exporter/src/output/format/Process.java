package output.format;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Process {

	private ArrayList<String> formattedOutput;

	private int line;

	private ResultSet resultSet;

	public Process(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public String readLine() {
		return null;
	}

}
