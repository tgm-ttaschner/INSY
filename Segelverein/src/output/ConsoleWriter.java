package output;

import java.util.ArrayList;

/**
 * @author Thomas Taschner
 * @version 19.01.2015
 * 
 * Diese Klasse liest das Ergebnis der Abfrage Fach fuer Fach aus und gibt es auf der Konsole aus.
 */
public class ConsoleWriter implements Writer {

	/* (non-Javadoc)
	 * @see output.Writer#write(java.util.ArrayList)
	 * 
	 * Nimmt die Ergebnisse des Resultset in String Listenform entgegen und gibt sie Fach fuer Fach auf der Konsole aus.
	 */
	@Override
	public void write(ArrayList<String> results) {
		for (int i = 0; i < results.size(); i++)	{
			System.out.println(results.get(i));
		}
	}
}