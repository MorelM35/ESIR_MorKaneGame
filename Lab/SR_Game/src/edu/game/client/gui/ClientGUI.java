package edu.game.client.gui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.game.client.ClientImpl;
import edu.game.client.ClientInt;

public class ClientGUI extends Composite  {
	private ClientImpl _controle;
	
	public ClientGUI(ClientImpl controle) {
		_controle = controle;
		
		VerticalPanel vPanel = new VerticalPanel();
		initWidget(vPanel);
		
		// TODO Init Window
		// answer from the method : _controle.getGrid() is the call of the method : update(grid) of this class
	}

	public void update(int[][] grid){
		// TODO : update Grid with this new grid
	}
}
