package ssteinkellner.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Schreibende Klasse, die fuer Output in text-dateien zustaendig ist
 * @author SSteinkellner
 * @version 2014.12.30
 */
public class FileWriter implements Writer{
	private File file;
	
	
	public FileWriter(String fileName){
		file = new File(fileName);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				printInternalException(e,"Exception when Creating File!");
			}
		}
	}

	@Override
	public void printLine(String text) {
		printToFile(text);
	}

	@Override
	public void printError(String text) {
		printToFile("[ERROR]\t"+text);
	}

	@Override
	public void printException(Exception e) {
		printToFile("[EXCEPTION]\t"+e);
	}
	
	private void printToFile(String text){
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new java.io.FileWriter(file, true)))) {
		    out.println(text);
		}catch (IOException e) {
			printInternalException(e,"Exception when Printing to File!");
		}
	}
	
	private void printInternalException(Exception e, String message){
		ConsoleWriter cw = new ConsoleWriter();
		cw.printError(message);
		cw.printException(e);
	}
}
