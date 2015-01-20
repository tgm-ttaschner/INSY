package ssteinkellner.connection;

/**
 * Exception fuer den fall, dass eine versuchte Injection erkannt wird
 * @author SSteinkellner
 * @version 2015.01.16
 *
 */
public class InjectionSafetyException extends Exception {
	public InjectionSafetyException() { super(); }
	public InjectionSafetyException(String message) { super(message); }
	public InjectionSafetyException(String message, Throwable cause) { super(message, cause); }
	public InjectionSafetyException(Throwable cause) { super(cause); }
}
