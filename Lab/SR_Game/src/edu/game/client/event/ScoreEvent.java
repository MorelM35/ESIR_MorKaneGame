package edu.game.client.event;

import de.novanic.eventservice.client.event.Event;

public class ScoreEvent implements Event {
	protected short[] _score = null;
	 
	public ScoreEvent(){}
	
    public short[] getScore() {
        return _score;
    }
 
    public void setMessage(short[] score) {
        this._score = score;
    }
 
    public ScoreEvent(short[] score){
        super();
 
        this._score = score;
    }
}
