package ssteinkellner.connection;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ssteinkellner.connection.ConnectionHandler;
import ssteinkellner.connection.UserCache;

public class Test_ConnectionHandler {
	UserCache uc;
	ConnectionHandler ch;

	@Before
	public void setUp() {
		ch = new ConnectionHandler(uc);
	}
	
	@Test
	public void test_Host() {
		String host = "127.0.0.1";
		ch.setHost(host);
		assertEquals(host, ch.getHost());
	}
	
	@Test
	public void test_Database() {
		String db = "schokoladenfabrik";
		ch.setDatabase(db);
		assertEquals(db, ch.getDatabase());
	}

}
