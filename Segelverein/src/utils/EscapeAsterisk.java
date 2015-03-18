package utils;

import java.io.File;
import java.util.*;

/**
 * @author Thomas Taschner
 * @version 19.01.2015
 * 
 * Diese Klasse dient einzig zur Behebung des '*' Eingabefehlers auf der Konsole (nur unter Windows, soweit mir bekannt).
 * 
 * Hierzu wird eine Liste mit allen Datei- und Ordnernamen des aktuellen Verzeichnisses hergenommen,
 * diese und die uebergebenen Konsolenargumente in eine ArrayList gepackt und und die Eintraege der Namensliste aus der Argumentenliste entfernt.
 * 
 * Sollte ein * in der Abfrage vorhanden gewesen sein (Check, ob ein bestimmtes Argument nun fehlt), wird nach dem '-f' Schalter ein * eingefuegt.
 * 
 * Abschliessend werden die nun veraenderten Argumente in ein neues String Array gespeichert und je nachdem, ob min. 1 Schalter angegeben wurde, werden entweder die neuen, veraenerten oder die orginalen Argumente zurueckgegeben.
 *
 */
public class EscapeAsterisk {
	
	/**
	 * @param args die Konsoleneingabeparameter, die ueberprueft werden sollen
	 * @return die bereinigten Parameter, sollte ein * in der Abfrage vorkommen, sonst nur die originalen
	 */
	public static String[] cleanup(String[] args)	{
		
		ArrayList<String> files = new ArrayList<String>();
		
		File[] filenames = new File(".").listFiles();
		
		/**
		 * Speichern der Datei- und Verzeichnisnamen in ner Liste.
		 */
		for (int i = 0; i < filenames.length; i++) {
			files.add(filenames[i].getName());
		}
		
		List<String> list = new LinkedList<String>();
		
		/**
		 * Speichern der eingegebenen Konsolenargumente in ner Liste.
		 */
		for (int i = 0; i < args.length; i++) {
			list.add(args[i]);
		}
		
		/**
		 * Loeschen aller Vorkommnisse aus der Namensliste in der Argumentenliste.
		 */
		list.removeAll(files);
		
		/**
		 * Fuege einen * ein, sollte einer im Original vorhanden gewesen sein.
		 */
		if (list.get(list.lastIndexOf("-f")+1).startsWith("-"))	{
			list.add(list.lastIndexOf("-f")+1, "*");
		}
		
		String[] copy = new String[list.size()];
		
		/**
		 * Zurueckkopieren der modifizierten Argumentenliste in ein neues String Array.
		 */
		for (int i = 0; i < list.size(); i++) {
			copy[i] = list.get(i);
			
		}
		
		/**
		 * Wenn mindestens 1 Schalten eingegeben wurde, gibt die modifizierte Konsoleneingabe zurueck, sonst die originale.
		 * 
		 * Hat nen technischen Grund, muss man nicht verstehen :)
		 */
		if (!args[0].matches("^-?://"))	{
			return copy;
		} else	{
			return args;
		}
	}
}