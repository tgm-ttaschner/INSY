package output;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class FileWriter implements Writer {

	private RandomAccessFile file;

	public FileWriter(String filename) throws FileNotFoundException {
		file = new RandomAccessFile(filename, "w");
	}

	@Override
	public void write(ArrayList<String> results) {
		for (int i = 0; i < results.size(); i++)	{
			try {
				file.writeUTF(results.get(i));
			} catch (IOException e) {
				System.out.println("Datei kann nicht beschrieben werden");
			}
		}
		
		try {
			file.close();
		} catch (IOException e) {
			System.out.println("Datei kann nicht geschlossen werden");
		}
	}
}