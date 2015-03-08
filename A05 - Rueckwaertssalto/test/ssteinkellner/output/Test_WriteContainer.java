package  ssteinkellner.output;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ssteinkellner.output.ConsoleWriter;
import ssteinkellner.output.WriteContainer;

public class Test_WriteContainer {
	private ByteArrayOutputStream outContent, errContent;
	private PrintStream out, err;
	private WriteContainer wc;
	
	@Before
	public void setUp() {
		out = System.out;
		err = System.err;
		
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
		errContent = new ByteArrayOutputStream();
		System.setErr(new PrintStream(errContent));
		
		wc = new WriteContainer();
		wc.addWriter(new ConsoleWriter());
		
	}

	@After
	public void tearDown() {
		System.setOut(out);
		System.setErr(err);
	}
	
	@Test
	public void test_printLine() {
		String text = "test";
		wc.printLine(text);
		
		assertEquals(text + "\r\n", outContent.toString());
	}

	@Test
	public void test_printError() {
		String text = "test";
		wc.printError(text);
		
		assertEquals(text + "\r\n", errContent.toString());
	}
	
	@Test
	public void test_printException() {
		Exception e = new Exception("test");
		wc.printException(e);
		
		assertEquals(e + "\r\n", errContent.toString());
	}
}
