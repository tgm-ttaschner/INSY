package ssteinkellner.tools;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ssteinkellner.tools.ListTools;

public class Test_ListTools {
	private List<String> values;
	
	@Before
	public void setUp() {
		values = new LinkedList<String>();
		values.add("value1");
		values.add("value2");
		values.add("value3");
	}
	
	@Test
	public void test_NoDelimiter() {
		String text = ListTools.listToString(values);
		
		assertEquals("value1, value2, value3",text);
	}
	
	@Test
	public void test_Delimiter() {
		String text = ListTools.listToString(values, ";");
		
		assertEquals("value1;value2;value3",text);
	}
}
