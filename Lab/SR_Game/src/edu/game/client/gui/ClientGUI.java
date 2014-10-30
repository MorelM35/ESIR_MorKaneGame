package edu.game.client.gui;


import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.game.client.ClientImpl;


public class ClientGUI extends Composite  {
	private ClientImpl _controle;
	private TextArea txt ;
	private Label label;
	private VerticalPanel vPanel = new VerticalPanel();
	TextArea txtScore;


	public ClientGUI(ClientImpl controle) {
		_controle = controle;
		
		initWidget(vPanel);
		
		label=new Label("Spectator ...");
		vPanel.add(label);
		
		// Init window TextArea
		txt= new TextArea();
		txt.setPixelSize(1000,380);
		txt.setAlignment(TextAlignment.CENTER);
		txt.setReadOnly(true);
		txt.setText("Loading...");
		vPanel.add(txt);
		
		txt.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				switch(event.getNativeKeyCode()){
				case KeyCodes.KEY_UP:
					_controle.moveUp();
					break;
				case KeyCodes.KEY_DOWN:
					_controle.moveDown();
					break;
				case KeyCodes.KEY_LEFT:
					_controle.moveLeft();
					break;
				case KeyCodes.KEY_RIGHT:
					_controle.moveRight();
					break;
				case KeyCodes.KEY_A:
					_controle.play();
					break;
				}
			}
		});
		
		// Init table of score
		txtScore = new TextArea();
		txtScore.setPixelSize(300,100);
		txtScore.setReadOnly(true);
		txtScore.setText("Loading Score...");
		vPanel.add(txtScore);
		
		// TODO Init Window
		//_controle.getGrid();
		// answer from the method : _controle.getGrid() is the call of the method : update(grid) of this class
	}

	/**
	 * Set the name of player 
	 * @param name
	 */
	public void setNameOfPlayer(String name){
		if(name=="-1") name="Spectator";
		label.setText("> Player "+name);
	}
	
	/**
	 * Update the grid of game
	 * @param newGrid : 
	 */
	public void updateGrid(byte[][] newGrid){
		int dim = newGrid.length;
		String s="";
		for(int i=0;i<dim;i++){
			for(int j=0; j<dim;j++){
				byte var = newGrid[j][i];
				switch(var){
				case 0:
					s+=" \t";
					break;
				case 1:
					s+=var+"\t";
					break;
				case 2:
					s+="X\t";
					break;
				default:
					s+=var+"\t";
				}				
			}
			s+="\n";
		}	
		txt.setText(s);
	}
	

	public void updateScore(short[] result) {
		String s = "~~~~~  Table Of Score  ~~~~~";
		s+="\n¤ Player 1 : "+result[0];		
		s+="\n¤ Player 2 : "+result[1];	
		s+="\n¤ Player 3 : "+result[2];	
		s+="\n¤ Player 4 : "+result[3];	
		
		//System.out.println(s);
		txtScore.setText(s);
	}
	
	public void showEndGame(String winnerPlayer){		
		// TODO : if player want re-play
		DialogBox dialog = new DialogBox(true);
		dialog.setTitle("!!! Game Over !!!!");
		dialog.setText("The winner of this game is "+winnerPlayer+"\n Congratulation!\n Click on OK to continue");
		dialog.showRelativeTo(txt);

	}

}
