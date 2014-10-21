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
		_controle.getGrid();
		// answer from the method : _controle.getGrid() is the call of the method : update(grid) of this class
	}

	public void update(int[][] grid){
		// TODO : update Grid with this new grid
		//System.out.println("Grid Length : "+grid.length);
		int dim = grid.length;
		System.out.println("############################# Grille Textuelle #############################");
		for(int i=0;i<dim;i++){
			String s="";
			for(int j=0; j<dim;j++){
				s+=grid[i][j]+"\t";
			}
			System.out.println(s);
		}	
		System.out.println("############################################################################");
	}
}
