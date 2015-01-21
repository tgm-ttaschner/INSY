package run;

import cli.ArgumentParser;

/**
 * @author Thomas Taschner
 * @version 19.01.2015
 * 
 */
public class Main {
	public static void main(String[] args) {
		ArgumentParser ap = new ArgumentParser(args);
		
		Controller c = new Controller(ap.getArguments());
	}
}