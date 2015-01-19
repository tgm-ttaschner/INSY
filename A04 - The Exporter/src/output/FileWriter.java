package output;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Thomas Taschner
 * @version 19.01.2015
 * 
 * Diese Klasse nimmt das formatierte Ergebnis der Abfrage entgegen und schreibt es Zeile fuer Zeile in die Datei hinein.
 * 
 * Sollte sich das File nicht beschreiben oder schliessen lassen, so wird eine entsprechende Fehlermeldung auf der Konsole ausgegeben.
 */
public class FileWriter implements Writer {

	private RandomAccessFile file;

	/**
	 * @param filename der Name der Datei, in die reingeschrieben werden soll
	 * @throws FileNotFoundException wird geworfen, wenn die Datei nicht gefunden werden kann
	 * 
	 * Initialisiert ein RandomAccessFile mit dem gegebenen Namen und setzt den Zugriffsmodus auf lesen, schreiben.
	 */
	public FileWriter(String filename) throws FileNotFoundException {
		file = new RandomAccessFile(filename, "rw");
	}

	/* (non-Javadoc)
	 * @see output.Writer#write(java.util.ArrayList)
	 * 
	 * Nimmt eine ArrayList mit dem formatierten Ergebnis der Abfrage entgegen und schreibt dieses in die neu erstellte Datei.
	 * 
	 * Sollte sich das File nicht beschreiben oder schliessen lassen, so wird eine entsprechende Fehlermeldung auf der Konsole ausgegeben.
	 */
	@Override
	public void write(ArrayList<String> results) {
		for (int i = 0; i < results.size(); i++)	{
			try {
				file.writeUTF(results.get(i) + "\n");
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