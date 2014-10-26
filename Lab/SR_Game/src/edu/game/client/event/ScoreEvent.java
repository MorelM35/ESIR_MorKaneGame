package edu.game.client.event;

import de.novanic.eventservice.client.event.Event;

public class ScoreEvent implements Event {
	protected String message = null;
	 
	public ScoreEvent(){}
	
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
 
    public ScoreEvent(String message){
        super();
 
        this.message = message;
    }
}
