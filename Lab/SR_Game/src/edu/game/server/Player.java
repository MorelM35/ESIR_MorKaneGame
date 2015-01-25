package edu.game.server;

public class Player {
	private byte 	_ID;
	private String 	_sessionID;
	private int[]	_coord;
	private short 	_score;
	
	public Player(byte nID, int coord[], String sessionID, short score) {
		_ID=nID;
		_coord=coord;
		_sessionID=sessionID;
		_score=score;
	}

	public int[] get_coord() {
		return _coord;
	}

	public void set_coord(int[] _coord) {
		this._coord = _coord;
	}

	public short get_score() {
		return _score;
	}

	public void set_score(short _score) {
		this._score = _score;
	}

	public byte get_ID() {
		return _ID;
	}
	
	public void set_sessionID(String session) {
		_sessionID=session;
	}

	public String get_sessionID() {
		return _sessionID;
	}
	
	public String toString(){
		return ""+get_ID()+";"+_coord[0]+"_"+_coord[1]+";"+get_sessionID()+";"+get_score();
	}
}
