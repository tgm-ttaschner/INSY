package ssteinkellner.tools;

import java.util.Iterator;
import java.util.List;

/**
 * eine klasse mit methoden, um listen in strings umzuwandeln
 * @author SSteinkellner
 * @version 2015.01.06
 */
public class ListTools {
	/**
	 * verwandelt eine string-liste in eine beistrichgetrennte auflistung als string
	 * @param values liste der strings
	 * @return eine beistrichgetrennte liste der strings
	 */
	public static String listToString(List<String> values){
		return listToString(values, ", ");
	}
	
	/**
	 * verwandelt eine string-liste in eine delimitergetrennte auflistung als string
	 * @param values liste der strings
	 * @param delimiter zeichen, das als trennzeichen verwendet werden soll
	 * @return eine delimitergetrennte liste der strings
	 */
	public static String listToString(List<String> values, String delimiter){
		String text = "";
		
		Iterator<String> i = values.iterator();
		while(i.hasNext()){
			if(!text.isEmpty()){ text += delimiter; }
			text += i.next();
		}
		
		return text;
	}
}
