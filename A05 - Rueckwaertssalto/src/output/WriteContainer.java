package ssteinkellner.output;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Container fuer mehrere Schreibende klassen
 * (um konsole und file zu kombinieren)
 * <br>Composite Pattern
 * @author SSteinkellner
 * @version 2014.12.30
 */
public class WriteContainer implements Writer{
	private List<Writer> list;
	
	public WriteContainer(){
		list = new LinkedList<Writer>();
	}
	
	public void addWriter(Writer writer){
		list.add(writer);
	}
	
	public void removeWriter(Writer writer){
		if(list.contains(writer)){
			list.remove(writer);
		}
	}
	
	public void removeWriter(int index){
		if(index >= list.size() || index < 0){
			throw new IndexOutOfBoundsException("Expected Index: 0 - " + (list.size()-1) + ". got Index: " + index);
		}
		
		list.remove(index);
	}
	
	public String listWriters(){
		String text = "";
		Iterator<Writer> i = list.iterator();
		while(i.hasNext()){
			if(!text.isEmpty()){ text+= ", "; }
			text += i.next().getClass().getSimpleName();
		}

		return text;
	}

	@Override
	public void printLine(String text) {
		Iterator<Writer> i = list.iterator();
		while(i.hasNext()){
			i.next().printLine(text);
		}
	}

	@Override
	public void printError(String text) {
		Iterator<Writer> i = list.iterator();
		while(i.hasNext()){
			i.next().printError(text);
		}
	}

	@Override
	public void printException(Exception e) {
		Iterator<Writer> i = list.iterator();
		while(i.hasNext()){
			i.next().printException(e);
		}
	}
}
