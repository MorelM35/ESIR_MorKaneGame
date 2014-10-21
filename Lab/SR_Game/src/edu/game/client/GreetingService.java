package edu.game.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("game")
public interface GreetingService extends RemoteService {
	
	// Different Move offered to Client
	boolean moveUp();
	boolean moveDown();
	boolean moveLeft();
	boolean moveRight();
	
	// Test between us
	int[][] getGrid();
	
}
