package edu.game;

import static org.junit.Assert.*;

import org.cyberneko.html.HTMLScanner.PlaybackInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.game.server.GreetingServiceImpl;

public class ServerSideTest {

	GreetingServiceImpl _server;

	/**
	 * MAKE TEST ON SCORES
	 */


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

	/**
	 * Test isInRange Method
	 * If the next coordinate stay in of range of the grid
	 */
	@Test
	public void isInRange(){
		int[] coord = new int[2];
		coord[0]=GreetingServiceImpl._gridx-1;
		coord[1]=GreetingServiceImpl._gridy-1;
		assertEquals(true,_server.isInOfRange(coord));
		coord[0]=GreetingServiceImpl._gridx+1;
		coord[1]=GreetingServiceImpl._gridy+1;
		assertEquals(false,_server.isInOfRange(coord));
		coord[0]=GreetingServiceImpl._gridx-1;
		coord[1]=GreetingServiceImpl._gridy+1;
		assertEquals(false,_server.isInOfRange(coord));
		coord[0]=GreetingServiceImpl._gridx+1;
		coord[1]=GreetingServiceImpl._gridy-1;
		assertEquals(false,_server.isInOfRange(coord));
		coord[0]=0;
		coord[1]=0;
		assertEquals(true,_server.isInOfRange(coord));
		coord[0]=-1;
		coord[1]=-1;
		assertEquals(false,_server.isInOfRange(coord));
	}

	/**
	 * Test isPossibleToMove method
	 * if there aren't 2 players in the same coordinate
	 */
	@Test
	public void isPossibleToMove(){
		isInRange();
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

	/**
	 * Test newPlayer method
	 */
	@Test 
	public void newPlayer(){
		byte id = _server.registerMe();
		assertEquals(1,id);
		id = _server.registerMe();
		assertEquals(2,id);
		id = _server.registerMe();
		assertEquals(3,id);
		id = _server.registerMe();
		assertEquals(4,id);
		id = _server.registerMe();
		assertEquals(-1,id);
	}

	/**
	 * Test RemovePlayer method
	 */
	@Test
	public void removePlayer(){
		try {
			_server.disconnectMe((byte)2);
		} catch (Exception e) {
			newPlayer();
			try {
				_server.disconnectMe((byte)2);
				_server.disconnectMe((byte)3);
				assertEquals(2,_server.registerMe());
				assertEquals(3,_server.registerMe());
				assertEquals(-1,_server.registerMe());
				return;
			} catch (Exception e1) {
				fail();
			}
		}
		fail("remove sucess with no player in game");
	}

	/**
	 * Test add Player in Grid
	 * If in the 4 corners of grid, All players are sotcked
	 */
	@Test
	public void test_getGrid(){
		byte[][] grid = _server.getGrid();
		assertEquals("Init Position player 1 failed",0,grid[0][0]);
		assertEquals("Init Position player 2 failed",0,grid[0][GreetingServiceImpl._gridy-1]);
		assertEquals("Init Position player 3 failed",0,grid[GreetingServiceImpl._gridx-1][0]);
		assertEquals("Init Position player 4 failed",0,grid[GreetingServiceImpl._gridx-1][GreetingServiceImpl._gridy-1]);
		newPlayer();
		assertEquals("Init Register player 1 failed",11,grid[0][0]);
		assertEquals("Init Register player 4 failed",14,grid[0][GreetingServiceImpl._gridy-1]);
		assertEquals("Init Register player 3 failed",13,grid[GreetingServiceImpl._gridx-1][0]);
		assertEquals("Init Register player 2 failed",12,grid[GreetingServiceImpl._gridx-1][GreetingServiceImpl._gridy-1]);
	}

	/**
	 * Test Init of Cookies and Mines.
	 * if there are the good number of cookies and mines
	 */
	@Test
	public void test_InitCookiesAndMines(){
		byte[][] grid = _server.getGrid();

		for(int k=0;k<100;k++){
			int nbCookies = 0;
			int nbMines = 0;
			_server.init();
			for(int i =0;i<GreetingServiceImpl._gridx;i++){
				for(int j=0;j<GreetingServiceImpl._gridy;j++){
					byte pos = grid[i][j];
					if(pos==1) nbCookies++;
					if(pos==2) nbMines++;
				}
			}
			assertEquals("Init Cookies failed",GreetingServiceImpl._nbInitCookies,nbCookies);
			assertEquals("Init Mines failed",GreetingServiceImpl._nbInitMines,nbMines);
		}
	}


	@After
	public void after(){
		_server=null;
	}

}
