package output;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class FileWriter implements Writer {

	private RandomAccessFile file;

	public FileWriter(String filename) throws FileNotFoundException {
		file = new RandomAccessFile(filename, "w");
	}


	/**
	 * @see output.Writer#write(java.lang.String)
	 * 
	 *  
	 */
	public void write(String line) {

	}

}
