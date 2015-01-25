package edu.game.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;
import de.novanic.eventservice.client.event.listener.unlisten.UnlistenEvent;
import de.novanic.eventservice.client.event.listener.unlisten.UnlistenEventListener.Scope;
import de.novanic.eventservice.client.event.listener.unlisten.UnlistenEventListenerAdapter;
import edu.game.client.event.GameOverEvent;
import edu.game.client.event.MoveEvent;
import edu.game.client.event.ScoreEvent;
import edu.game.client.event.newPlayerEvent;
import edu.game.client.event.removePlayerEvent;
import edu.game.client.gui.ClientGUI;

/**
 * Attempts to reconnect the GWTEventService to the server. You may invoke this calls as often as
 * you want. If the number of calls exceeds the object's internal counter (max reconnect attempts)
 * it will display an error message instead. Hence, whenever a connection failure is detected
 * somewhere in the application the reconnector should be called to do its magic.
 */
public class Reconnector {

	private static final int MAX_RECONNECT_ATTEMPTS = 100;
	private static final int RECONNECT_DELAY_BASIS_MILLIS = 2000;
	private int reconnectAttemptsCounter = 0;
	private ClientGUI _gui;
	private ClientImpl _controle;

	/**
	 * C'tor.
	 */
	public Reconnector(ClientGUI gui, ClientImpl controle) {
		_gui = gui;
		_controle=controle;
		addUnlistenListener();
	}

	private void addUnlistenListener() {
		getRemoteService().addUnlistenListener(Scope.LOCAL, new UnlistenEventListenerAdapter() {

			@Override
			public void onUnlisten(UnlistenEvent unlistenEvent) {
				GWT.log("UnlistenEvent occured: isTimeout=" + unlistenEvent.isTimeout() + " isLocal="
						+ unlistenEvent.isLocal() + " userId="
						+ (unlistenEvent.getUserId() == null ? "" : unlistenEvent.getUserId()) + " domains="
						+ (unlistenEvent.getDomains() == null ? "" : unlistenEvent.getDomains()));
				reconnect();
			}
		}, null);
	}

	/**
	 * Attempts to reestablish standing connection to the server for the event listener.
	 */
	public void reconnect() {
		_gui.showProblem("Attempts to connect with the server ... ("+reconnectAttemptsCounter+")");
		reconnectAttemptsCounter++;
		if (maxReconnectAttemptsReached()) {
			GWT.log("Number of reconnect attempts " + reconnectAttemptsCounter
					+ " above max retry limit (" + MAX_RECONNECT_ATTEMPTS
					+ ") -> giving up and displaying error message.");
			displayNoConnectionError();
		} else {
			GWT.log("Number of reconnect attempts " + reconnectAttemptsCounter
					+ " below or equal the max retry attempts (" + MAX_RECONNECT_ATTEMPTS
					+ ") -> reconnecting.");
			attemptReconnect();
		}
	}

	private void attemptReconnect() {
		final Timer timer = new Timer() {

			@Override
			public void run() {
				initGwtEventService();
			}
		};

		timer.schedule(calculateNextReconnectDelay());
	}

	private void initGwtEventService() {
		final RemoteEventService remoteService = getRemoteService();
		remoteService.removeListeners();
		remoteService.removeUnlistenListeners(null);

		remoteService.addListener(GreetingService.SERVER_MESSAGE_DOMAIN, new RemoteEventListener() {
			@Override
			public void apply(Event anEvent) {
				if(anEvent instanceof MoveEvent){ 			// If Event on movement of players
					_gui.updateGrid(((MoveEvent)anEvent).getGrid());
				}else if (anEvent instanceof ScoreEvent){ 	// If event on update of score
					_gui.updateScore(((ScoreEvent)anEvent).getScore());
				}else if (anEvent instanceof GameOverEvent){ // If event on End Game
					_gui.showEndGame(((GameOverEvent)anEvent).getMessage());
					_controle.isOver();
					_gui.setNameOfPlayer((byte)-1);
				}else if (anEvent instanceof newPlayerEvent){
					_gui.addNewPlayer(((newPlayerEvent) anEvent).getID(),((newPlayerEvent) anEvent).getCoord());
				}else if (anEvent instanceof removePlayerEvent){					
					_gui.removeAplayer(((removePlayerEvent) anEvent).getID(),((removePlayerEvent) anEvent).getCoord());
				}else{
					_gui.showProblem("Erreur RemoteEventService::Apply");
				}
			}
		});
		
		addUnlistenListener();
	}

	private int calculateNextReconnectDelay() {
		return ((int) Math.pow(reconnectAttemptsCounter, 2.0)) * RECONNECT_DELAY_BASIS_MILLIS;
	}

	private boolean maxReconnectAttemptsReached() {
		return reconnectAttemptsCounter > MAX_RECONNECT_ATTEMPTS;
	}

	private RemoteEventService getRemoteService() {
		return RemoteEventServiceFactory.getInstance().getRemoteEventService();
	}

	private void displayNoConnectionError() {
		_gui.showProblem("NoConnectionError");
	}

	public void resetCounter() {
		reconnectAttemptsCounter=0;		
	}
}

