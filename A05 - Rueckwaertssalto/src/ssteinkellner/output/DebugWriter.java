package ssteinkellner.output;

/**
 * Schreibende Klasse, die fuer debug-output zustaendig ist
 * <br>(gibt erweiterte informationen auf der konsole aus)
 * @author SSteinkellner
 * @version 2014.12.30
 */
public class DebugWriter implements Writer{

	@Override
	public void printLine(String text) {
		System.out.println("[DEBUG]["+getPosition()+"]\t" + text);
	}

	@Override
	public void printError(String text) {
		System.err.println("[ERROR]["+getPosition()+"]\t" + text);
	}

	@Override
	public void printException(Exception e) {
		System.err.println("[EXCEPTION]["+getPosition()+"]");
		e.printStackTrace();
	}
	
	/**
	 * gibt die position der aufrufenden methode zurueck
	 * @return position der aufrufenden methode
	 */
	private static String getPosition(){
		StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
		String pos = ste.getClassName() + ":" + ste.getLineNumber();
		return pos;
	}
}
