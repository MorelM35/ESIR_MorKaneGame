package edu.game.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.game.client.gui.ClientGUI;

public class ClientImpl implements ClientInt{
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync _service;
	
	private ClientGUI _vue;
	
	public ClientImpl(String url) {
		System.out.println("url : "+url);
		this._service = GWT.create(GreetingService.class);
		ServiceDefTarget endPoint = (ServiceDefTarget) this._service;
		endPoint.setServiceEntryPoint(url);
		this._vue = new ClientGUI(this);
	}

	@Override
	public void moveUp() {
		_service.moveUp(new DefaultCallBack());
	}

	@Override
	public void moveDown() {
		_service.moveDown(new DefaultCallBack());		
	}

	@Override
	public void moveLeft() {
		_service.moveLeft(new DefaultCallBack());
		
	}

	@Override
	public void moveRight() {
		_service.moveRight(new DefaultCallBack());
		
	}

	@Override
	public void getGrid() {
		_service.getGrid(new DefaultCallBack());
	}
	
	private void update(int[][] grid){
		_vue.update(grid);
	}
	
	public ClientGUI getGUI() {
		return _vue;
	}
	
	public class DefaultCallBack implements AsyncCallback{

		@Override
		public void onFailure(Throwable caught) {
			System.err.println("ERROR : "+caught.getMessage());
			
		}

		@Override
		public void onSuccess(Object result) {
			if(result instanceof int[][]){
				System.out.println("Tableau");
				update((int[][])result);
			}else {
				System.out.println("Nothing");
			}
		}
		
	}
}
