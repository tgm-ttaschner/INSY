package ssteinkellner.output;

/**
 * Schreibende Klasse, die fuer debug-output zustaendig ist
 * <br>(gibt erweiterte informationen auf der konsole aus)
 * @author SSteinkellner
 * @version 2014.12.30
 */
public class DebugWriter implements Writer{
	private Writer writer;
	
	public DebugWriter(Writer writer){
		this.writer = writer;
	}
	
	
	@Override
	public void printLine(String text) {
		writer.printLine("["+getPosition()+"]\t" + text);
	}

	@Override
	public void printError(String text) {
		writer.printError("["+getPosition()+"]\t" + text);
	}

	@Override
	public void printException(Exception e) {
		writer.printError("[EXCEPTION]\t["+getPosition()+"]" + e);
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
