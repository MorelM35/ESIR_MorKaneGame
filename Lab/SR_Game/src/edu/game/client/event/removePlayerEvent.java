package edu.game.client.event;

import de.novanic.eventservice.client.event.Event;

public class removePlayerEvent implements Event {
	protected int[] _coord = null;
	private byte _id = -1;
	 
	public removePlayerEvent(){}
	
    public int[] getCoord() {
        return _coord;
    }
    
    public byte getID(){
    	return _id;
    }
 
    public void setCoord(int[] coord) {
        this._coord = coord;
    }
 
    public removePlayerEvent(byte id , int[] coord){
        super();
        this._id=id;
        this._coord = coord;
    }
}
