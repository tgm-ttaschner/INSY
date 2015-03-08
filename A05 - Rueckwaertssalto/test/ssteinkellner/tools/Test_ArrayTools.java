package ssteinkellner.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ssteinkellner.tools.ArrayTools;

public class Test_ArrayTools {
	private String[] values;
	
	@Before
	public void setUp() throws Exception {
		values = new String[]{"value1","value2","value3"};
	}

	@Test
	public void test_NoDelimiter() {
		String text = ArrayTools.arrayToString(values);
		
		assertEquals("value1, value2, value3",text);
	}
	
	@Test
	public void test_Delimiter() {
		String text = ArrayTools.arrayToString(values, ";");
		
		assertEquals("value1;value2;value3",text);
	}

}
