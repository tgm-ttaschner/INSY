package run;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

import cli.ArgumentParser;
import connection.MySQLConnection;
import output.*;
import output.Writer;
import output.format.Process;
import utils.EscapeAsterisk;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, FileNotFoundException, IllegalArgumentException {

		try {
			HashMap<String, String> arguments = new HashMap<String, String>();

			ArgumentParser parser = new ArgumentParser(EscapeAsterisk.cleanup(args));

			arguments = parser.getArguments();


			MySQLConnection c = new MySQLConnection(arguments);
			c.connect();
			c.query();



			Process p = new Process(c.getResultSet(), arguments.get("seperator"));

			WriterFactory wf = new WriterFactory();
			Writer w = wf.chooseWriter(arguments.get("output"), "abc.txt");
			w.write(p.readAll());

			c.disconnect();

		} catch (Exception e)	{
			e.printStackTrace();
		}
	}
}