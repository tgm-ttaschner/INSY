package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EscapeAsterisk {
	public static String[] cleanup(String[] args)	{
		
		ArrayList<String> files = new ArrayList<String>();
		
		File[] filenames = new File(".").listFiles();
		
		for (int i = 0; i < filenames.length; i++) {
			files.add(filenames[i].getName());
		}
		
		List<String> list = new LinkedList<String>();
		
		for (int i = 0; i < args.length; i++) {
			list.add(args[i]);
		}
		
		list.removeAll(files);
		
		list.add(list.lastIndexOf("-f")+1, "*");
		
		String[] copy = new String[list.size()];
		
		for (int i = 0; i < list.size(); i++) {
			copy[i] = list.get(i);
			
		}
		
		return copy;
		
	}
}
