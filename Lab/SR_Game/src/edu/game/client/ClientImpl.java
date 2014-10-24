package edu.game.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.game.client.gui.ClientGUI;


public class ClientImpl implements ClientInt{

	private static boolean debug = true;


	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync _service;

	private ClientGUI _vue;
	private byte _myID =0;

	public ClientImpl(String url) {
		System.out.println("url : "+url);
		this._service = GWT.create(GreetingService.class);
		ServiceDefTarget endPoint = (ServiceDefTarget) this._service;
		endPoint.setServiceEntryPoint(url);
		this._vue = new ClientGUI(this);

		_service.registerMe(new getID_CallBack());
		getGrid();
	}

	@Override
	public void moveUp() {
		_service.moveUp(_myID,new DefaultCallBack());
	}

	@Override
	public void moveDown() {
		_service.moveDown(_myID,new DefaultCallBack());		
	}

	@Override
	public void moveLeft() {
		_service.moveLeft(_myID,new DefaultCallBack());

	}

	@Override
	public void moveRight() {
		_service.moveRight(_myID,new DefaultCallBack());

	}

	@Override
	public void getGrid() {
		_service.getGrid(new DefaultCallBack());
	}

	public ClientGUI getGUI() {
		return _vue;
	}

	private void _trace(String msg){
		if(debug){
			System.out.println("DEBUG : "+msg);
		}
	}
	
	@Override
	public void getScore() {
		_service.getScore(new DefaultCallBack());		
	}

	// ##############################################################
	// ######################	CallBack	#########################
	// ##############################################################

	public class getID_CallBack implements AsyncCallback{

		@Override
		public void onFailure(Throwable caught) {
			System.err.println("ERROR : "+caught.getMessage());
		}

		@Override
		public void onSuccess(Object result) {
			if(result instanceof Byte){
				_myID= (Byte) result;
				_vue.setNameOfPlayer(""+_myID);
				_trace("myID = "+_myID);
			}
		}

	}


	public class DefaultCallBack implements AsyncCallback{

		@Override
		public void onFailure(Throwable caught) {
			System.err.println("ERROR : "+caught.getMessage());
		}

		@Override
		public void onSuccess(Object result) {
			if(result instanceof byte[][]){	
				_vue.updateGrid((byte[][])result);
			}else if (result instanceof Boolean){
				// TODO Maybe delete when WebSocket
				getGrid();
				getScore();
			}else if (result instanceof short[]){
				_vue.updateScore((short[])result);
			}else{
				_trace("DefaultCallBack Nothing");
			}
		}

	}

}
