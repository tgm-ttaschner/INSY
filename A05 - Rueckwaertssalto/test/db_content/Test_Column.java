package db_content;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class Test_Column {
	private Column c;

	@Before
	public void setUp() {
		c = new Column("test");
	}

	@Test
	public void name() {
		assertEquals("test",c.getColumnName());
	}
	
	@Test
	public void checkPrimary(){
		c.setPrimary(true);
		assertEquals(true,c.isPrimary());
	}

	@Test
	public void notForeignColumn(){
		assertEquals(false,c.isForeign());
	}
	
	@Test
	public void foreignColumn(){
		String referenceName = "testReference";
		
		c.setForeign(referenceName);
		assertEquals(true,c.isForeign());
		assertEquals(referenceName,c.getForeign());
	}
}
