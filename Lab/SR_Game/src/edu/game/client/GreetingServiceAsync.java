package edu.game.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	
	void moveUp(byte myID,AsyncCallback callback);
	void moveDown(byte myID,AsyncCallback callback);
	void moveLeft(byte myID,AsyncCallback callback);
	void moveRight(byte myID,AsyncCallback callback);
	
	void getGrid(AsyncCallback callback);
	void registerMe(AsyncCallback callback);
}
