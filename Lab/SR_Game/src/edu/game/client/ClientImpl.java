package edu.game.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import edu.game.client.gui.ClientGUI;

public class ClientImpl implements ClientInt{
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync service;
	
	private ClientGUI _vue;
	
	public ClientImpl(String url) {
		System.out.println("url : "+url);
		this.service = GWT.create(GreetingService.class);
		ServiceDefTarget endPoint = (ServiceDefTarget) this.service;
		endPoint.setServiceEntryPoint(url);
		this._vue = new ClientGUI(this);
	}

	@Override
	public void moveUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveRight() {
		// TODO Auto-generated method stub
		
	}
}
