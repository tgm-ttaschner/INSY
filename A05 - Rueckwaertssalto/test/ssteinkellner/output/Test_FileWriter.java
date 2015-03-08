package  ssteinkellner.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ssteinkellner.output.FileWriter;

public class Test_FileWriter {
	private FileWriter fw;
	private String fileName;
	
	@Before
	public void setUp() throws Exception {
		fileName = "test.txt";
		fw = new FileWriter(fileName);
	}
	
	@Test
	public void test_printLine() {
		String text = "test";
		
		File f = new File(fileName);
		f.delete();	//file loeschen, um eine leere datei zu haben
		
		fw.printLine(text);
		
		if(!f.exists()){ fail("File doesn't exist!"); }
		
		try (
				BufferedReader br = new BufferedReader(new FileReader(fileName));
				){
			assertEquals(text,br.readLine());
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_printError() {
		String text = "test";
		
		File f = new File(fileName);
		f.delete();	//file loeschen, um eine leere datei zu haben
		
		fw.printError(text);
		
		if(!f.exists()){ fail("File doesn't exist!"); }
		
		try (
				BufferedReader br = new BufferedReader(new FileReader(fileName));
				){
			assertEquals("[ERROR]\t"+text,br.readLine());
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void test_printException() {
		Exception e = new Exception("test");
		
		File f = new File(fileName);
		f.delete();	//file loeschen, um eine leere datei zu haben
		
		fw.printException(e);
		
		if(!f.exists()){ fail("File doesn't exist!"); }
		
		try (
				BufferedReader br = new BufferedReader(new FileReader(fileName));
			){
			assertEquals("[EXCEPTION]\t"+e,br.readLine());
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
