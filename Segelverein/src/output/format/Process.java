package output.format;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Thomas Taschner
 * @version 19.01.2015
 * 
 * In dieser Klasse wird das Ergebnis der Abfrage (Resultset) Zeile fuer Zeile, Spalte fuer Spalte ausgelesen,
 * jeweils immer mit einem, in der Konsole eingegebenen Trennzeichen getrennt und Zeile fuer Zeile in eine ArrayList gespeichert.
 * 
 * Sollte keine Verbindung zur Datenbank hergestellt werden koennnen (-> null Resultset), so wird eine SQLException geworfen.
 */
public class Process {

	private ArrayList<String> formattedOutput;

	private String seperator;

	private ResultSet resultSet;

	private ResultSetMetaData rsmd;

	/**
	 * @param resultSet das ResultSet, in dem das Ergebnis der Abfrage gespeichert ist
	 * @param seperator das Trennzeichen, das zum Trennen der einzelnen Datensaetze verwendet wird
	 * @throws SQLException wird geworfen, wenn keine Verbindung zur Datenbank hergestellt werden konnte
	 * 
	 * Initialisiert die ArrayList und holt die Metadaten des ResultSets.
	 */
	public Process(ResultSet resultSet, String seperator) throws SQLException {
		this.resultSet = resultSet;
		this.seperator = seperator;

		formattedOutput = new ArrayList<String>();

		rsmd = resultSet.getMetaData();
	}

	/**
	 * @return die formatierte Ausgabe des Ergebnisses der Abfrage
	 * @throws SQLException wird geworfen, wenn keine Verbindung zur Datenbank hergestellt werden konnte
	 * 
	 * Geht das ResultSet der Abfrage durch und speichert es formatiert in eine ArrayList.
	 */
	public ArrayList<String> readAll() throws SQLException {
		while (resultSet.next())	{

			String column = "";

			for (int i = 1; i <= rsmd.getColumnCount(); i++)	{
				if (i == rsmd.getColumnCount())	{
					column += resultSet.getObject(i);
				} else {
					column += resultSet.getObject(i) + seperator + " ";
				}
			}

			formattedOutput.add(column);
		}

		return formattedOutput;
	}
}