package edu.game.client.event;

import de.novanic.eventservice.client.event.Event;

public class EndGameEvent implements Event {
	protected String _winner = null;
	 
	public EndGameEvent(){}
	
    public String getMessage() {
        return _winner;
    }
 
    public void setMessage(String winner) {
        this._winner = winner;
    }
 
    public EndGameEvent(String winner){
        super();
 
        this._winner = winner;
    }
}
