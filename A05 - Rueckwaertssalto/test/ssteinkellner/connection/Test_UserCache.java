package ssteinkellner.connection;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ssteinkellner.connection.UserCache;

public class Test_UserCache {
	private UserCache uc;

	@Before
	public void setUp() {
		uc = new UserCache();
	}
	
	@Test
	public void test_User() {
		String name = "test";
		uc.setUser(name);
		assertEquals(name, uc.getUser());
	}

	@Test
	public void test_Password() {
		String pwd = "test";
		uc.setPassword(pwd);
		assertEquals(pwd, uc.getPassword());
	}
}
