package edu.game.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("game")
public interface GreetingService extends RemoteService {
	
	// Different Move offered to Client
	boolean moveUp(byte myID);
	boolean moveDown(byte myID);
	boolean moveLeft(byte myID);
	boolean moveRight(byte myID);
	
	// Test between us
	byte[][] getGrid();
	short[] getScore();
	
	byte registerMe();
	
}
