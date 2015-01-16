package output;

import java.io.RandomAccessFile;

public class FileWriter extends WriterFactory implements Writer {

	private RandomAccessFile file;

	public FileWriter(String filename) {
		
	}


	/**
	 * @see output.Writer#write(java.lang.String)
	 * 
	 *  
	 */
	public void write(String line) {

	}

}
