package edu.game.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	
	void moveUp(AsyncCallback callback);
	void moveDown(AsyncCallback callback);
	void moveLeft(AsyncCallback callback);
	void moveRight(AsyncCallback callback);
	
	void getGrid(AsyncCallback callback);
}
