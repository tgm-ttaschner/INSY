package  ssteinkellner.output;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class Test_DebugWriter {
	private Writer w, wp;
	private String printedText;

	@Before
	public void setUp() throws Exception {
		wp = new Writer(){
			@Override
			public void printLine(String text) {
				printedText = text;
			}

			@Override
			public void printError(String text) {
				printedText = text;
			}

			@Override
			public void printException(Exception e) {
				printedText = ""+e;
			}
		};
		w = new DebugWriter(wp);
	}

	@Test
	public void test_printLine() {
		String text = "test";
		w.printLine(text);
		
		int lineNumber = 36;
		String expect = "["+this.getClass().getName()+":"+lineNumber+"]\t"+text;
		
		assertEquals(expect,printedText);
	}
	
	@Test
	public void test_printError() {
		String text = "test";
		w.printError(text);
		
		int lineNumber = 47;
		String expect = "["+this.getClass().getName()+":"+lineNumber+"]\t"+text;
		
		assertEquals(expect,printedText);
	}
/*	
	@Test
	public void test_printException() {
		String text = "test";
		Exception e = new Exception(text);
		w.printException(e);
		
		int lineNumber = 59;
		String expect = "[EXCEPTION]["+this.getClass().getName()+":"+lineNumber+"]";
		
		assertEquals(expect,printedText.split(""+e)[0]);
	}
*/	
	@Test
	public void test_printException() {
		String text = "test";
		Exception e = new Exception(text);
		w.printException(e);
		
		int lineNumber = 71;
		String expect = "[EXCEPTION]\t["+this.getClass().getName()+":"+lineNumber+"]"+e;
		
		assertEquals(expect,printedText);
	}
}
