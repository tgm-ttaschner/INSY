package ssteinkellner.output;

/**
 * Schreibende Klasse, die fuer Output auf der Konsole zustaendig ist
 * @author SSteinkellner
 * @version 2014.12.30
 */
public class ConsoleWriter implements Writer{

	@Override
	public void printLine(String text) {
		System.out.println(text);
	}

	@Override
	public void printError(String text) {
		System.err.println(text);
	}

	@Override
	public void printException(Exception e) {
		System.err.println(e);
	}
}
