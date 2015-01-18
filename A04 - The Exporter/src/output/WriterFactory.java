package output;

import java.io.FileNotFoundException;

public class WriterFactory {

	public Writer chooseWriter(String type, String filename) throws FileNotFoundException {
		switch (type) {
		case "file":
			return new FileWriter(filename);
		case "console":
			return new ConsoleWriter();
		default:
			return null;
		}
	}
}