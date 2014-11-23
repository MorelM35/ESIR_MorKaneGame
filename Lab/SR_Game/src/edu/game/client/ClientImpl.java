package edu.game.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;
import edu.game.client.event.GameOverEvent;
import edu.game.client.event.MoveEvent;
import edu.game.client.event.ScoreEvent;
import edu.game.client.event.newPlayerEvent;
import edu.game.client.event.removePlayerEvent;
import edu.game.client.gui.ClientGUI;


public class ClientImpl implements ClientInt{

	private static boolean debug = true;

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync _service;

	private ClientGUI 	_vue;
	private byte 		_myID;

	public ClientImpl(String url) {
		System.out.println("url : "+url);
		this._service = GWT.create(GreetingService.class);
		ServiceDefTarget endPoint = (ServiceDefTarget) this._service;
		endPoint.setServiceEntryPoint(url);
		this._vue = new ClientGUI(this);

		// Init du Serveur Pushing
		RemoteEventService remote = RemoteEventServiceFactory.getInstance().getRemoteEventService();
		remote.addListener(GreetingService.SERVER_MESSAGE_DOMAIN, new RemoteEventListener() {
			@Override
			public void apply(Event anEvent) {
				if(anEvent instanceof MoveEvent){ 			// If Event on movement of players
					//_trace("Event : new Movement");
					_vue.updateGrid(((MoveEvent)anEvent).getGrid());
				}else if (anEvent instanceof ScoreEvent){ 	// If event on update of score
					_vue.updateScore(((ScoreEvent)anEvent).getScore());
				}else if (anEvent instanceof GameOverEvent){ // If event on End Game
					_vue.showEndGame(((GameOverEvent)anEvent).getMessage());
					_myID=-1;
					_vue.setNameOfPlayer((byte)-1);
					getGrid();
				}else if (anEvent instanceof newPlayerEvent){
					_vue.addNewPlayer(((newPlayerEvent) anEvent).getID(),((newPlayerEvent) anEvent).getCoord());
				}else if (anEvent instanceof removePlayerEvent){
					_vue.removeAplayer(((removePlayerEvent) anEvent).getID(),((removePlayerEvent) anEvent).getCoord());
				}else{
					System.err.println("Erreur RemoteEventService::Apply");
				}
			}
		});

		// Add Listener on Closing window
		Window.addWindowClosingHandler(new Window.ClosingHandler() {
			@Override
			public void onWindowClosing(ClosingEvent event) {
				try {_service.disconnectMe(_myID, new DefaultCallBack());} catch (Exception e) {e.printStackTrace();}
			}
		});

		_myID=-1;
		getGrid();
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
				// TODO 
			}else if (result instanceof short[]){
				_vue.updateScore((short[])result);
			}else{
				_trace("DefaultCallBack Nothing");
			}
		}

	}

}
