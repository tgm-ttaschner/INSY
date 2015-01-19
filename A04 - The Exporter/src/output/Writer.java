package output;

import java.util.ArrayList;

/**
 * @author Thomas Taschner
 * @version 19.01.2015
 * 
 * Writer Interface, das fuer das Schreiben des Ergebnisses der Abfrage verwendet wird.
 * Die entgegengenommene ArrayList wird Zeile fuer Zeile ausgegeben.
 */
public interface Writer {
	/**
	 * @param results die ArrayList, die ausgelesen werden soll
	 * 
	 * Gibt die ArrayList (auf der Konsole oder im File) aus.
	 */
	public abstract void write(ArrayList<String> results);
}