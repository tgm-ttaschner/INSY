package ssteinkellner.tools;

/**
 * eine klasse mit methoden, um arrays in Strings umzuandeln
 * @author SSteinkellner
 * @version 2015.01.07
 */
public class ArrayTools {
	/**
	 * verwandelt eine string-liste in eine beistrichgetrennte auflistung als string
	 * @param values liste der strings
	 * @return eine beistrichgetrennte liste der strings
	 */
	public static String arrayToString(String[] values){
		return arrayToString(values, ", ");
	}
	
	/**
	 * verwandelt eine string-liste in eine delimitergetrennte auflistung als string
	 * @param values liste der strings
	 * @param delimiter zeichen, das als trennzeichen verwendet werden soll
	 * @return eine delimitergetrennte liste der strings
	 */
	public static String arrayToString(String[] values, String delimiter){
		String text = "";
		
		for(int i=0;i<values.length;i++){
			if(!text.isEmpty()){ text += delimiter; }
			text += values[i];
		}
		
		return text;
	}
}