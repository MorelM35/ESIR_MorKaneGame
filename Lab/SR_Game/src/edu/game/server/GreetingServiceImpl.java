package edu.game.server;


import java.util.Random;
import java.util.HashMap;

import com.google.gwt.dev.shell.BrowserChannel.SessionHandler.ExceptionOrReturnValue;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.service.RemoteEventServiceServlet;
import edu.game.client.GreetingService;
import edu.game.client.event.EndGameEvent;
import edu.game.client.event.MoveEvent;
import edu.game.client.event.ScoreEvent;


/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteEventServiceServlet implements GreetingService {
	// Variables
	private final short _nbMaxPlayers = 4;
	private final int _gridx = 20;
	private final int _gridy = 20;
	private final int _nbInitCookie = 200;
	
	private int _nbCookies;
	private HashMap<Byte,int[]> _mapPlayers;
	private byte[][] _grid;
	private short[] _score;

	/**
	 * Constructor avec This server
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
		initGrid(_nbInitCookie);
		for(int i=0;i<_nbMaxPlayers;i++){
			_score[i]=0;
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
			_grid[coord[0]][coord[1]]=0;
			_score[myID-1]=0;
			_mapPlayers.remove(myID);
			System.out.println("remove Player "+myID);
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
		return _nbCookies;
	}

	private boolean isInOfRange(int [] coord){
		return !(coord[0]<0 || coord[0]>_gridx-1 || coord[1]>_gridy-1 || coord[1]<0);
	}

	public boolean isPossibleToMove(int[] coord){
		if(isInOfRange(coord)){
			//System.out.println("try to move ("+coord[0]+":"+coord[1]+")");
			return _grid[coord[0]][coord[1]] <2;
		} else return false;
	}
	
	/**
	 * When a player eat a cookie on the grid
	 * @param nID
	 */
	private void eatCookie(byte nID){
		_score[nID-1]++;
		_nbCookies--;
		// Send info to Client
		sendToClients(new ScoreEvent(_score));
		if(_nbCookies<=0){
			byte winner = 0;
			short score = 0;
			for(byte i=0;i<_score.length;i++){
				if(_score[i]>score){
					score = _score[i];
					winner = i;
				}
			}
			sendToClients(new EndGameEvent("Player "+winner));
		}
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
			if(_grid[newCoord[0]][newCoord[1]]==1){ // There is a cookie
				eatCookie(myID);
			}
			_grid[oldCoord[0]][oldCoord[1]] = 0;
			_grid[newCoord[0]][newCoord[1]] = (byte) (myID+10);
			_mapPlayers.put(myID, newCoord);
			
			Event event = new MoveEvent(_grid);
			sendToClients(event);
			return true;
		}
		return false;
	}
	
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
