package edu.game.client.event;

import de.novanic.eventservice.client.event.Event;

public class MoveEvent implements Event {
	protected byte[][] _grid = null;
	 
	public MoveEvent(){}
	
    public byte[][] getGrid() {
        return _grid;
    }
 
    public void setMessage(byte[][] grid) {
        this._grid = grid;
    }
 
    public MoveEvent(byte[][] grid){
        super();
 
        this._grid = grid;
    }
}
