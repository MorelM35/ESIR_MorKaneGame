package edu.game.client.gui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.game.client.ClientImpl;
import edu.game.client.ClientInt;

public class ClientGUI extends Composite implements ClientInt {
	private ClientImpl _controle;
	
	public ClientGUI(ClientImpl controle) {
		_controle = controle;
		
		VerticalPanel vPanel = new VerticalPanel();
		initWidget(vPanel);
		
		// TODO Auto-generated constructor stub
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
