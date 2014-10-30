package edu.game.client.event;

import de.novanic.eventservice.client.event.Event;

public class GameOverEvent implements Event {
	protected String _winner = null;
	 
	public GameOverEvent(){}
	
    public String getMessage() {
        return _winner;
    }
 
    public void setMessage(String winner) {
        this._winner = winner;
    }
 
    public GameOverEvent(String winner){
        super();
 
        this._winner = winner;
    }
}
