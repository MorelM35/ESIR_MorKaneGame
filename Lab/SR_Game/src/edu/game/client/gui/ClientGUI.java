package edu.game.client.gui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.game.client.ClientImpl;
import edu.game.client.ClientInt;

public class ClientGUI extends Composite  {
	private ClientImpl _controle;
	private TextArea txt ;

	public ClientGUI(ClientImpl controle) {
		_controle = controle;

		VerticalPanel vPanel = new VerticalPanel();
		initWidget(vPanel);
		txt= new TextArea();
		txt.setText("Loading...");
		vPanel.add(txt);

		// TODO Init Window
		//_controle.getGrid();
		// answer from the method : _controle.getGrid() is the call of the method : update(grid) of this class
	}

	public void update(byte[][] result){
		// TODO : update Grid with this new grid
		//showInConsole(result);
		int dim = result.length;
		String s="";
		for(int i=0;i<dim;i++){
			for(int j=0; j<dim;j++){
				s+=result[i][j]+"\t";
			}
			s+="\n";
		}	
		txt.setText(s);
	}
	
	public void showInConsole(byte[][] result){
		int dim = result.length;
		String s="";
		System.out.println("############################# Grille Textuelle #############################");
		for(int i=0;i<dim;i++){
			for(int j=0; j<dim;j++){
				s+=result[i][j]+"\t";
			}
			s+="\n";
		}	
		System.out.println(s+"############################################################################");
	}

}
