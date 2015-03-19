package output;

import java.io.FileNotFoundException;

/**
 * @author Thomas Taschner
 * @version 19.01.2015
 * 
 * Fabrik, in der entscheiden wird, wie die Ausgabe des Ergebnisses der Abfrage erfolgen soll.
 * 
 * Hierzu werden ein (Ausgabe) Typ und ein Dateiname uebernommen und anhand dieser Parameter der Ausgabetyp (und eventuell der Dateiname) festgelegt.
 * 
 * Im Fall von 'console' erfolgt die Ausgabe auf der Konsole, sonst in einer Datei mit dem gegebenen Namen.
 * 
 * Es wird eine FileNotFoundException geworfen, sollte die Datei nicht gefunden werden koennnen.
 */
public class WriterFactory {
	
	/**
	 * @param type der Ausgabetyp des Ergebnisses der Abfrage
	 * @param filename der Dateiname, sollte die Ausgabe in die Datei gespeichert werden sollen
	 * @return einen Writer eines bestimmten Typs
	 * @throws FileNotFoundException wird geworfen, sollte die zu beschreibende Datei nicht gefunden werden
	 * 
	 * Waehlt anhand des Inputs die Art der Ausgabe.
	 */
	public Writer chooseWriter(String type, String filename) throws FileNotFoundException {
		/*
		switch (type) {
		case "console":
			return new ConsoleWriter();
		default:
			return new FileWriter(filename);
		}
		*/
		return new ConsoleWriter();
	}
}