package ssteinkellner.output;

/**
 * Interface fuer alle schreibenden klassen
 * <br>Strategy Pattern
 * @author SSteinkellner
 * @version 2014.12.30
 */
public interface Writer {
	
	/**
	 * gibt einen text aus
	 * @param text text, der ausgegeben werden soll
	 */
	public void printLine(String text);
	
	/**
	 * gibt eine gekennzeichnete fehlermeldung aus
	 * @param text fehler, der ausgegeben werden soll
	 */
	public void printError(String text);
	
	/**
	 * gibt einen, die Exception beschreibenden, text aus
	 * @param e exception, die ausgegeben werden soll
	 */
	public void printException(Exception e);
}
