package edu.game.server;


import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.commons.el.EnumeratedMap;

import com.google.appengine.api.search.query.ExpressionParser.str_return;
import com.google.gwt.core.ext.ServletContainer;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;
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
	public static final int ID_COOKIE = 1;
	public static final int ID_MINE = 2;
	public static final int ID_BONUS = 3;
	
	// Variables
	public static final short _nbMaxPlayers = 4;
	public static final int _gridx = 50;
	public static final int _gridy = 20;
	public static final int _nbInitCookies = 100;
	public static final int _nbInitMines = 70;
	public static final int _nbInitBonus = 5;
	public static final short _penaltyPoint = 5;
	public static final short _bonusPoint = 5;
	/**
	 * l'ID certain qu'il n'y a pas de joueur
	 */
	public static final short _freeID  = (_nbInitMines * _penaltyPoint*-1)-1; // WARNING : check size of 'short'

	private int _nbCookies;
	private HashMap<Byte,Player> _mapPlayers;
	private int			_nbConnectedPlayer;
	private byte[][] _grid;
	private short[] _score;

	/**
	 * Constructor 
	 */
	public GreetingServiceImpl() {
		System.out.println("\n##########################");
		System.out.println("# \t MorKaneGame \t #");
		System.out.println("##########################\n");
		
		_score = new short[_nbMaxPlayers];
		
		// Check if config file exist
		if(BackUp.isCrashed()){
			System.out.println(" >> Active After Crash");
			_grid = BackUp.restoreGrid();
			System.out.println("_ grid restored");
			_mapPlayers = BackUp.restoreMapPlayer();
			System.out.println("_ map Players restored "+_mapPlayers.size());
			_nbConnectedPlayer = _mapPlayers.size();
			for(int i=0;i<_nbMaxPlayers;i++){
				_score[i]=_freeID;
			}
			for(Player p : _mapPlayers.values()){
				_score[p.get_ID()-1] = p.get_score();
			}
			calculateNbCookieFromGrid();
			System.out.println("_ score restored");			
		} else {
			System.out.println(" >> Active");
			_grid=new byte[_gridx][_gridy];
			_mapPlayers = new HashMap<Byte,Player>();
			initGame();
		}
	}

	/**
	 * Init Grid
	 */
	public void initGame(){
		BackUp.resetBackUp();
		initGrid(_nbInitCookies+_nbInitBonus);
		_mapPlayers.clear();
		_nbConnectedPlayer = 0;
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
		System.out.println("Info Request : "+getRequest().getRequestedSessionId());

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

		System.out.println("new Player "+nID+" : ["+coord[0]+"]["+coord[1]+"]");
		_grid[coord[0]][coord[1]]=(byte) (nID+10);
		_score[nID-1]=0;

		Player player = new Player(nID, coord, getRequest().getRequestedSessionId(),(short) 0);
		_mapPlayers.put(nID, player);

		connectPlayer(nID,coord);
		BackUp.saveClient(player);

		return coord;
	}

	/**
	 * Remove player of the game
	 * @param myID	: ID player
	 * @throws Exception 
	 */
	private void removePlayer(byte myID) throws Exception {
		Player me = _mapPlayers.get(myID);
		int[] coord= me.get_coord();
		if(coord!=null){
			//System.out.println("try to remove ("+coord[0]+":"+coord[1]+")="+_grid[coord[0]][coord[1]]);
			_grid[coord[0]][coord[1]]=0;
			//_score[myID-1]=_freeID;
			sendToClients(new removePlayerEvent(myID,coord));
			// If there are not player anymore
			System.out.println("# Remove player "+me.get_ID()+" from the grid ");
			_nbConnectedPlayer--;
			if(_nbConnectedPlayer==0){
				initGame();
			}
		} else throw new Exception("Wrong ID");

	}

	/**
	 * Connect and notify Others Players of new player
	 * @param nID	:	ID of the new player
	 * @param coord	:	coord of new player
	 */
	private void connectPlayer(byte nID, int[] coord){
		sendToClients(new newPlayerEvent(nID,coord));
		_nbConnectedPlayer++;
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
		
		int nbBonus = 0;
		_nbCookies=0;
		while (nbBonus!=_nbInitBonus){
			int x = _gridx/4 + r.nextInt(_gridx/2);	
			int y = _gridy/4 + r.nextInt(_gridy/2);
			
			if(!(x==0 & (y==0 |y==_gridy-1))){		// if the place is not a place for player
				if(!(x==_gridx-1 & (y==0 |y==_gridy-1))){
					if(_grid[x][y]==0){				// If the place is empty
						_grid[x][y]=ID_BONUS;
						nbBonus++;
						_nbCookies++;
					}
				}
			}
		}
		
		while(_nbCookies!=nbCookies){
			int x = r.nextInt(_gridx);	
			int y = r.nextInt(_gridy);
			if(!(x==0 & (y==0 |y==_gridy-1))){		// if the place is not a place for player
				if(!(x==_gridx-1 & (y==0 |y==_gridy-1))){
					if(_grid[x][y]==0){				// If the place is empty
						_grid[x][y]=ID_COOKIE;
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
						_grid[x][y]=ID_MINE;
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
		Player p = _mapPlayers.get(nID);
		p.set_score((short)(p.get_score()+1));
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
		Player p = _mapPlayers.get(nID);
		p.set_score((short)(p.get_score()-_penaltyPoint));
		_score[nID-1]-=_penaltyPoint;

		// Send info to Client
		sendToClients(new ScoreEvent(_score));
	}
	
	/**
	 * When a player eat a bonus on the grid and check the end of game
	 * @param nID : ID of player
	 */
	private void eatBonus(byte nID) {
		Player p = _mapPlayers.get(nID);
		p.set_score((short)(p.get_score()+_bonusPoint));
		_score[nID-1]+=_bonusPoint;
		_nbCookies--;
		// Send info to Client
		sendToClients(new ScoreEvent(_score));
	}

	/**
	 * Send the winner of this game, disconnect all players and initialise a new grid
	 */
	private void gameOver(){
		Player winner;
		// Search winner
		Collection<Player> players =  _mapPlayers.values();
		Iterator<Player> itPlayer =  players.iterator();
		winner = itPlayer.next();

		while(itPlayer.hasNext()){
			Player player = itPlayer.next();
			if(player.get_score()>winner.get_score()){
				winner=player;
			}
		}
		System.out.println("###############");
		// remove the players
		_mapPlayers.clear();

		initGame();

		// to Inform Player
		sendToClients(new GameOverEvent("Player "+(winner.get_ID())));
	}

	/**
	 * Move the player in the grid if it's possible
	 * @param myID 		: ID of player
	 * @param oldCoord 	: Localisation of player
	 * @param newCoord 	: next move of player
	 * @return			: if player had moved
	 */
	private Boolean movePlayerTo(byte myID, int[] oldCoord,int[] newCoord){
		if(isPossibleToMove(newCoord)){
			Player me = _mapPlayers.get(myID);
			me.set_coord(newCoord);
			byte place = _grid[newCoord[0]][newCoord[1]];
			if(place==ID_COOKIE){ 			// There is a cookie
				eatCookie(myID);
			} else if(place ==ID_MINE){	// There is a mine
				eatMine(myID);			
			}else if(place ==ID_BONUS){
				eatBonus(myID);
			}
			_grid[oldCoord[0]][oldCoord[1]] = 0;
			_grid[newCoord[0]][newCoord[1]] = (byte) (myID+10);
			
			me.set_sessionID(getRequest().getRequestedSessionId());
			
			BackUp.saveClient(me);
			Event event = new MoveEvent(_grid);
			
			if(!sendToClients(event)){
				return false;
			}

			BackUp.saveGrid(_grid);

			if(_nbCookies<=0){	
				gameOver();
			}
			return true;
		}
		return true;
	}


	public void printGrid(byte[][] grid){
		for(byte[] line : grid){
			String s = "";
			for(byte c : line){
				s+=""+c+"\t";
			}
			System.out.println(s);
		}
	}

	/**
	 * Send a broadcaste message to all connected clients 
	 * @param event
	 */
	private boolean sendToClients(Event event){
		try{
			addEvent(GreetingService.SERVER_MESSAGE_DOMAIN, event);
		} catch(Exception e){
			return false;
		}		
		return true;
	}
	
	/**
	 * FAULT TOLERANCE , calculate and initialize the number of cookie in this game
	 */
	private void calculateNbCookieFromGrid() {
		_nbCookies = 0;
		for(int i =0; i <_grid.length;i++){
			for(int j = 0; j <_grid[i].length;j++){
				if(_grid[i][j] == ID_COOKIE || _grid[i][j] ==ID_BONUS){
					_nbCookies++;
				}
			}
		}
	}


	// ##############################################################
	// ################		Call From Client 	#####################
	// ##############################################################

	/**
	 * Call from client to register player in the grid
	 * @return the player ID
	 */
	public byte registerMe(){
		// check if the player is already in the game (Fault Tolerance)
		Iterator<Player> it_p = _mapPlayers.values().iterator();
		while(it_p.hasNext()){
			Player p = it_p.next();
			if(getRequest().getRequestedSessionId().equals(p.get_sessionID())){
				connectPlayer(p.get_ID(), p.get_coord());
				return p.get_ID();
			}
		}		

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
	public Boolean moveUp(byte myID) {
		int [] myCoord = _mapPlayers.get(myID).get_coord();
		int [] newCoord = {myCoord[0],myCoord[1]-1};
		return movePlayerTo(myID, myCoord, newCoord);
	}

	@Override
	public Boolean moveDown(byte myID) {
		int [] myCoord = _mapPlayers.get(myID).get_coord();
		int [] newCoord = {myCoord[0],myCoord[1]+1};
		return movePlayerTo(myID, myCoord, newCoord);
	}

	@Override
	public Boolean moveLeft(byte myID) {
		int [] myCoord = _mapPlayers.get(myID).get_coord();
		int [] newCoord = {myCoord[0]-1,myCoord[1]};
		return movePlayerTo(myID, myCoord, newCoord);
	}

	@Override
	public Boolean moveRight(byte myID) {
		int [] myCoord = _mapPlayers.get(myID).get_coord();
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
