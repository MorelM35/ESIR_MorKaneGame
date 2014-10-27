package edu.game;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.game.server.GreetingServiceImpl;

public class ServerSideTest {

	GreetingServiceImpl _server;
	
	@Before 
	public void init(){
		_server=new GreetingServiceImpl();
	}
	
	@Test
	public void initCookie() {
		int nbCookies = 30;
		int res = _server.initGrid(nbCookies);
		assertEquals(nbCookies, res);
	}
	
	@Test
	public void isInRange(){
		int [] coord = new int [2];
		coord[0]=10;
		coord[1]=20;
		assertEquals(true,_server.isPossibleToMove(coord));
		coord[0]=-1;
		assertEquals(false,_server.isPossibleToMove(coord));
		coord[0]=20;
		assertEquals(false,_server.isPossibleToMove(coord));
		coord[1]=20;
		assertEquals(false,_server.isPossibleToMove(coord));
		coord[1]=-1;
		assertEquals(false,_server.isPossibleToMove(coord));
		coord[0]=10;
		assertEquals(false,_server.isPossibleToMove(coord));
		coord[0]=13;
		coord[1]=12;
		assertEquals(true,_server.isPossibleToMove(coord));
	}
	
	@After
	public void after(){
		_server=null;
	}

}
