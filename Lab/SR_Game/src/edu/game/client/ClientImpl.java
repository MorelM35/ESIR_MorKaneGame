package edu.game.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import edu.game.client.gui.ClientGUI;


public class ClientImpl implements ClientInt{

	private static boolean debug = true;

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync _service;

	private ClientGUI 	_vue;
	private byte 		_myID;
	private Reconnector _connector;

	public ClientImpl(String url) {
		System.out.println("url : "+url);
		this._service = GWT.create(GreetingService.class);
		ServiceDefTarget endPoint = (ServiceDefTarget) this._service;
		endPoint.setServiceEntryPoint(url);
		this._vue = new ClientGUI(this);

		// Init du Serveur Pushing

		// Add Listener on Closing window
		Window.addWindowClosingHandler(new Window.ClosingHandler() {
			@Override
			public void onWindowClosing(ClosingEvent event) {
				try {_service.disconnectMe(_myID, new DefaultCallBack());} catch (Exception e) {e.printStackTrace();}
			}
		});

		// Test the connectivity with the server 
		_connector = new Reconnector(_vue,this);
		final Timer timer = new Timer() {
			@Override
			public void run() {
				final RemoteEventService remoteEventService = RemoteEventServiceFactory.getInstance()
						.getRemoteEventService();
				if (remoteEventService.isActive()) {
					_vue.showProblem("");
					_connector.resetCounter();
					schedule(2000);
				} else {
					_vue.showProblem("Connection to the server failed");
					_connector.reconnect();
					schedule(1000);
				}
			}
		};
		timer.schedule(1000);

		isOver();
		getScore();
	}


	/**
	 * Register with the server
	 */
	public void play(){
		if(_myID<0){
			_service.registerMe(new getID_CallBack());
		}
	}

	@Override
	public void moveUp() {
		if(_myID>0){
			_service.moveUp(_myID,new DefaultCallBack());
		}
	}

	@Override
	public void moveDown() {
		if(_myID>0){
			_service.moveDown(_myID,new DefaultCallBack());	
		}	
	}

	@Override
	public void moveLeft() {
		if(_myID>0){
			_service.moveLeft(_myID,new DefaultCallBack());
		}

	}

	@Override
	public void moveRight() {
		if(_myID>0){
			_service.moveRight(_myID,new DefaultCallBack());
		}
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
				_vue.setNameOfPlayer(_myID);
				_trace("myID = "+_myID);
				getGrid();
				getScore();
			}
		}

	}


	public class DefaultCallBack implements AsyncCallback{

		@Override
		public void onFailure(Throwable caught) {
			System.err.println("ERROR : "+caught.getMessage());
			//_vue.showError(caught.getMessage());
		}

		@Override
		public void onSuccess(Object result) {		
			if(result instanceof byte[][]){	
				_vue.updateGrid((byte[][])result);
			}else if (result instanceof Boolean){
				Boolean val = (Boolean) result;
				//if(!val) _connector.reconnect();
			}else if (result instanceof short[]){
				_vue.updateScore((short[])result);
			}else{
				_trace("DefaultCallBack Nothing");
			}
		}
	}


	public void isOver() {
		_myID=-1;
		getGrid();
	}

}
