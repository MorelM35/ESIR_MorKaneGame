package edu.game.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("MessengerRemoteService")
public interface GreetingService extends RemoteService {
	// Give a domain to server
	public static final Domain SERVER_MESSAGE_DOMAIN = DomainFactory.getDomain("server_message_domain");
	
	// Init
	void disconnectMe(byte myID) throws Exception;
	
	// Different Move offered to Client
	Boolean moveUp(byte myID);
	Boolean moveDown(byte myID);
	Boolean moveLeft(byte myID);
	Boolean moveRight(byte myID);
	
	// Test between us
	byte[][] getGrid();
	short[] getScore();
	
	byte registerMe();
	
}
