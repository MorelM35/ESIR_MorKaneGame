package edu.game.server;


import java.util.Iterator;
import java.util.Random;
import java.util.HashMap;
import java.util.Set;

import com.google.gwt.dev.shell.BrowserChannel.SessionHandler.ExceptionOrReturnValue;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.service.RemoteEventServiceServlet;
import edu.game.client.GreetingService;
import edu.game.client.event.GameOverEvent;
import edu.game.client.event.MoveEvent;
import edu.game.client.event.ScoreEvent;
import edu.game.client.event.newPlayerEvent;
import edu.game.client.event.removePlayerEvent;


/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteEventServiceServlet implements GreetingService {
	// Variables
	public static final short _nbMaxPlayers = 4;
	public static final int _gridx = 50;
	public static final int _gridy = 20;
	public static final int _nbInitCookies = 100;
	public static final int _nbInitMines = 50;
	public static final short _penaltyPoint = 5;
	/**
	 * l'ID certain qu'il n'y a pas de joueur
	 */
	public static final short _freeID  = (_nbInitMines * _penaltyPoint*-1)-1; // WARNING : check size of 'short'

	private int _nbCookies;
	private HashMap<Byte,int[]> _mapPlayers;
	private byte[][] _grid;
	private short[] _score;

	/**
	 * Constructor 
	 */
	public GreetingServiceImpl() {
		_grid=new byte[_gridx][_gridy];
		_score = new short[_nbMaxPlayers];
		_mapPlayers = new HashMap<Byte,int[]>();
		init();
	}

	/**
	 * Init Grid
	 */
	public void init(){
		initGrid(_nbInitCookies);
		for(int i=0;i<_nbMaxPlayers;i++){
			_score[i]=_freeID;
		}
	}

	/**
	 * Insert new Player in the grid
	 * @param nID : ID of the new player
	 * @return localisation
	 */
	private int[] newPlayer(byte nID){
		int[] coord = new int[2];
		switch(nID){
		case 1:
			coord[0]=0;
			coord[1]=0;
			break;
		case 2:
			coord[0]=_gridx-1;
			coord[1]=_gridy-1;
			break;
		case 3:
			coord[0]=_gridx-1;
			coord[1]=0;
			break;
		case 4:
			coord[0]=0;
			coord[1]=_gridy-1;
			break;
		}

		_mapPlayers.put(nID, coord);
		System.out.println("new Player "+nID+" : ["+coord[0]+"]["+coord[1]+"]");
		_grid[coord[0]][coord[1]]=(byte) (nID+10);
		_score[nID-1]=0;
		
		sendToClients(new newPlayerEvent(nID,coord));

		return coord;
	}

	/**
	 * Remove player of the game
	 * @param myID	: ID player
	 * @throws Exception 
	 */
	private void removePlayer(byte myID) throws Exception {
		int[] coord= _mapPlayers.get(myID);
		if(coord!=null){
			//System.out.println("try to remove ("+coord[0]+":"+coord[1]+")="+_grid[coord[0]][coord[1]]);
			_grid[coord[0]][coord[1]]=0;
			_score[myID-1]=_freeID;
			if(_mapPlayers.remove(myID)==null)System.err.println("Player not found");
			else System.out.println("remove Player "+myID);
			sendToClients(new removePlayerEvent(myID,coord));
			// If there are not player anymore
			if(_mapPlayers.isEmpty()){
				init();
			}
		} else throw new Exception("Wrong ID");

	}

	/**
	 * Initialise the grid with cookies 
	 * @param nbCookies : number of cookies to put
	 */
	public int initGrid(int nbCookies){
		//Init empty grid
		for(int i=0;i<_gridx;i++){
			for(int j=0;j<_gridy;j++){
				_grid[i][j]=0;
			}			
		}
		// add cookies in the grid
		Random r = new Random();
		_nbCookies=0;
		while(_nbCookies!=nbCookies){
			int x = r.nextInt(_gridx);	
			int y = r.nextInt(_gridy);
			if(!(x==0 & (y==0 |y==_gridy-1))){		// if the place is not a place for player
				if(!(x==_gridx-1 & (y==0 |y==_gridy-1))){
					if(_grid[x][y]==0){				// If the place is empty
						_grid[x][y]=1;
						_nbCookies++;
					}
				}
			}
		}
		
		// add Mines in the game
		int nbMines = 0;
		while(nbMines!=_nbInitMines){
			int x = r.nextInt(_gridx);	
			int y = r.nextInt(_gridy);
			if(!(x==0 & (y==0 |y==_gridy-1))){		// if the place is not a place for player
				if(!(x==_gridx-1 & (y==0 |y==_gridy-1))){
					if(_grid[x][y]==0){				// If the place is empty
						_grid[x][y]=2;
						nbMines++;
					}
				}
			}
		}
		return _nbCookies;
	}

	/**
	 * Check is the coordinate is in of the grid
	 * @param coord : coordinate
	 * @return		: boolean if in or not
	 */
	public boolean isInOfRange(int [] coord){
		return !(coord[0]<0 || coord[0]>_gridx-1 || coord[1]>_gridy-1 || coord[1]<0);
	}

	/**
	 * Check is the case is free for a movement 
	 * @param coord
	 * @return
	 */
	public boolean isPossibleToMove(int[] coord){
		if(isInOfRange(coord)){
			//System.out.println("try to move ("+coord[0]+":"+coord[1]+")");
			return _grid[coord[0]][coord[1]] <5;
		} else return false;
	}

	/**
	 * When a player eat a cookie on the grid and check the end of game
	 * @param nID : ID of player
	 */
	private void eatCookie(byte nID){
		_score[nID-1]++;
		_nbCookies--;
		// Send info to Client
		sendToClients(new ScoreEvent(_score));
	}
	
	/**
	 * When a player eat a cookie on the grid and check the end of game
	 * @param nID : ID of player
	 */
	private void eatMine(byte nID){
		_score[nID-1]-=_penaltyPoint;
		// Send info to Client
		sendToClients(new ScoreEvent(_score));
	}

	/**
	 * Send the winner of this game, disconnect all players and initialise a new grid
	 */
	private void gameOver(){
		byte winner = 0;
		short score = 0;
		// Search winner
		Set<Byte> setPlayer = _mapPlayers.keySet();
		Iterator<Byte> itPlayer =  setPlayer.iterator();
		System.out.println("#### Game Over ####");
		winner = itPlayer.next();
		score = _score[winner-1];
		System.out.println("> p"+winner+" ["+score+"]");
		while(itPlayer.hasNext()){
			byte player = itPlayer.next();
			System.out.println("> p"+player+" ["+_score[player-1]+"]");
			player--;
			if(_score[player]>score){
				score=_score[player];
				winner=(byte) (player+1);
			}
		}
		System.out.println("###############");
		// remove the players
		_mapPlayers.clear();
		/*
		for(byte player:_mapPlayers.keySet()){
			try {
				removePlayer(player);
			} catch (Exception e) {e.printStackTrace();}
		}
		*/

		init();
		
		// to Inform Player
		sendToClients(new GameOverEvent("Player "+(winner)));
	}

	/**
	 * Move the player in the grid if it's possible
	 * @param myID 		: ID of player
	 * @param oldCoord 	: Localisation of player
	 * @param newCoord 	: next move of player
	 * @return			: if player had moved
	 */
	private boolean movePlayerTo(byte myID, int[] oldCoord,int[] newCoord){
		if(isPossibleToMove(newCoord)){
			_mapPlayers.put(myID, newCoord);
			byte place = _grid[newCoord[0]][newCoord[1]];
			if(place==1){ 			// There is a cookie
				eatCookie(myID);
			} else if(place ==2){	// There is a mine
				eatMine(myID);
			}
			_grid[oldCoord[0]][oldCoord[1]] = 0;
			_grid[newCoord[0]][newCoord[1]] = (byte) (myID+10);
			
			Event event = new MoveEvent(_grid);
			sendToClients(event);
			
			if(_nbCookies<=0){	
				gameOver();
			}
			return true;
		}
		return false;
	}

	/**
	 * Send a broadcaste message to all connected clients 
	 * @param event
	 */
	private void sendToClients(Event event){
		addEvent(GreetingService.SERVER_MESSAGE_DOMAIN, event);
	}


	// ##############################################################
	// ################		Call From Client 	#####################
	// ##############################################################

	/**
	 * Call from client to register player in the grid
	 * @return the player ID
	 */
	public byte registerMe(){
		if(_mapPlayers.size()<_nbMaxPlayers){
			for(byte i=1;i<_nbMaxPlayers+1;i++){
				if(!_mapPlayers.containsKey(i)){
					newPlayer(i);
					return i;
				}
			}
		}
		return -1;	// the grid is full of players
	}

	@Override
	public boolean moveUp(byte myID) {
		int [] myCoord = _mapPlayers.get(myID);
		int [] newCoord = {myCoord[0],myCoord[1]-1};
		return movePlayerTo(myID, myCoord, newCoord);
	}

	@Override
	public boolean moveDown(byte myID) {
		int [] myCoord = _mapPlayers.get(myID);
		int [] newCoord = {myCoord[0],myCoord[1]+1};
		return movePlayerTo(myID, myCoord, newCoord);
	}

	@Override
	public boolean moveLeft(byte myID) {
		int [] myCoord = _mapPlayers.get(myID);
		int [] newCoord = {myCoord[0]-1,myCoord[1]};
		return movePlayerTo(myID, myCoord, newCoord);
	}

	@Override
	public boolean moveRight(byte myID) {
		int [] myCoord = _mapPlayers.get(myID);
		int [] newCoord = {myCoord[0]+1,myCoord[1]};
		return movePlayerTo(myID, myCoord, newCoord);
	}

	@Override
	public byte[][] getGrid() {
		return _grid;
	}

	@Override
	public short[] getScore() {
		return _score;
	}

	@Override
	public void disconnectMe(byte myID) throws Exception {
		assert myID >0 				: "myID : Incorrect Data";
		assert myID <_nbMaxPlayers 	: "myID : Incorrect Data";

		removePlayer(myID);
	}

}
