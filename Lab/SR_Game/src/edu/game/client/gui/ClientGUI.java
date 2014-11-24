package edu.game.client.gui;


import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.game.client.ClientImpl;
import edu.game.server.GreetingServiceImpl;


public class ClientGUI extends Composite  {
	private ClientImpl _controle;
	private Label labelPlayer;
	private Label labelScore_p1;
	private Label labelScore_p2;
	private Label labelScore_p3;
	private Label labelScore_p4;
	private VerticalPanel vPanel = new VerticalPanel();
	private HorizontalPanel hScore;

	//private TextArea txtScore;
	private Context2d 		_context;
	private Canvas 			_canvas;
	private ImageElement 	imgCookie;
	private ImageElement 	imgMine;
	private ImageElement 	imgPlayer1;
	private ImageElement 	imgPlayer2;
	private ImageElement 	imgPlayer3;
	private ImageElement 	imgPlayer4;
	private Image			imgCurrentPlayer;
	private Image			imgScore_p1;
	private Image			imgScore_p2;
	private Image			imgScore_p3;
	private Image			imgScore_p4;

	private static final int width = 700;
	private static final int height= 300;
	private static final int sizeImg = 15;

	private static final String urlCookie =  "http://cakelair.net/public/style_extra/team_icons/rank_icon_cookie.png";
	private static final String urlMine = "http://www.rw-designer.com/icon-view/3078.png";
	private static final String urlP1 ="http://icons.iconarchive.com/icons/spoon-graphics/monster/32/Blue-Monster-icon.png";
	private static final String urlP2 ="http://icons.iconarchive.com/icons/spoon-graphics/monster/32/Green-Monster-icon.png";
	private static final String urlP3 ="http://icons.iconarchive.com/icons/spoon-graphics/monster/32/Orange-Monster-icon.png";
	private static final String urlP4 ="http://icons.iconarchive.com/icons/spoon-graphics/monster/32/Purple-Monster-icon.png";
	private static final String urlSpectator = "http://ec.l.thumbs.canstockphoto.com/canstock3826647.jpg";

	// Init
	int intervalx = width/GreetingServiceImpl._gridx;
	int intervaly = height/GreetingServiceImpl._gridy;


	public ClientGUI(ClientImpl controle) {

		_controle = controle;
		vPanel.addStyleName("center");
		initWidget(vPanel);
		vPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);


		// init Images
		imgCookie 	= ImageElement.as(new Image(urlCookie).getElement());
		imgMine 	= ImageElement.as(new Image(urlMine).getElement());		
		imgPlayer1	= ImageElement.as(new Image(urlP1).getElement());	
		imgPlayer2	= ImageElement.as(new Image(urlP2).getElement());	
		imgPlayer3	= ImageElement.as(new Image(urlP3).getElement());	
		imgPlayer4	= ImageElement.as(new Image(urlP4).getElement());
		imgCurrentPlayer = new Image(urlSpectator);
		imgCurrentPlayer.setSize(sizeImg+8+"pt", sizeImg+8+"pt");
		imgScore_p1 = new Image(urlP1);
		imgScore_p1.setSize(sizeImg+8+"pt", sizeImg+8+"pt");
		imgScore_p2 = new Image(urlP2);
		imgScore_p2.setSize(sizeImg+8+"pt", sizeImg+8+"pt");
		imgScore_p3 = new Image(urlP3);
		imgScore_p3.setSize(sizeImg+8+"pt", sizeImg+8+"pt");
		imgScore_p4 = new Image(urlP4);
		imgScore_p4.setSize(sizeImg+8+"pt", sizeImg+8+"pt");
		labelPlayer= new Label("Press 'Enter' to play");
		
		// Init Score
		labelScore_p1 = new Label("Player 1");
		labelScore_p1.setStyleName("scoreStyle");
		labelScore_p2 = new Label("Player 2");
		labelScore_p2.setStyleName("scoreStyle");
		labelScore_p3 = new Label("Player 3");
		labelScore_p3.setStyleName("scoreStyle");
		labelScore_p4 = new Label("Player 4");
		labelScore_p4.setStyleName("scoreStyle");

		vPanel.add(imgCurrentPlayer);
		vPanel.add(labelPlayer);

		// ########## Init Canvas
		_canvas = Canvas.createIfSupported();
		if(_canvas==null){
			DialogBox dialog = new DialogBox(true);
			dialog.setTitle("Error");
			dialog.setText("can't continue");
			dialog.showRelativeTo(vPanel);
		}
		vPanel.add(_canvas);
		_canvas.setWidth(width+"pt");
		_canvas.setHeight(height+"pt");
		_canvas.setCoordinateSpaceWidth(width);
		_canvas.setCoordinateSpaceHeight(height);
		_context = _canvas.getContext2d();


		// Init Key Handler
		_canvas.addKeyDownHandler(new KeyDownHandler() {
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
				case KeyCodes.KEY_ENTER:
					_controle.play();
					break;
				}
			}
		});
		
		hScore = new HorizontalPanel();
		hScore.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		hScore.setHorizontalAlignment(HorizontalPanel.ALIGN_JUSTIFY);
		vPanel.add(hScore);
	}

	/**
	 * Set the name of player 
	 * @param name
	 */
	public void setNameOfPlayer(byte nID){
		switch(nID){
		case -1:
			imgCurrentPlayer.setUrl(urlSpectator);
			labelPlayer.setText("The room is full !! You are a spectator (if you want try to play, press 'Enter')");
			break;
		case 1:
			imgCurrentPlayer.setUrl(urlP1);
			labelPlayer.setText("Player "+nID);
			break;
		case 2:
			imgCurrentPlayer.setUrl(urlP2);
			labelPlayer.setText("Player "+nID);
			break;
		case 3:
			imgCurrentPlayer.setUrl(urlP3);
			labelPlayer.setText("Player "+nID);
			break;
		case 4:
			imgCurrentPlayer.setUrl(urlP4);
			labelPlayer.setText("Player "+nID);
			break;
		}
	}

	/**
	 * Update the grid of game
	 * @param newGrid : 
	 */
	public void updateGrid(byte[][] newGrid){
		_context.clearRect(0, 0, width, height);
		int dim = newGrid.length;
		for(int i=0;i<dim;i++){
			for(int j=0; j<dim;j++){
				byte var = newGrid[j][i];
				switch(var){
				case 0:
					break;
				case 1:
					drawCookie(j,i);
					break;
				case 2:
					drawMine(j, i);
					break;
				default:
					drawPlayer(var-10, j, i);
				}				
			}
		}	
		_context.stroke();
	}


	public void updateScore(short[] result) {
		// Init Score
		String s;

		if(result[0]!=GreetingServiceImpl._freeID){
			hScore.add(imgScore_p1);
			s="1 : ["+result[0]+" Pts]   ";
			labelScore_p1.setText(s);
			hScore.add(labelScore_p1);
		} else {
			hScore.remove(labelScore_p1);
			hScore.remove(imgScore_p1);
		}

		if(result[1]!=GreetingServiceImpl._freeID){
			hScore.add(imgScore_p2);
			s="2 : ["+result[1]+" Pts]   ";
			labelScore_p2.setText(s);
			hScore.add(labelScore_p2);
		}else {
			hScore.remove(labelScore_p2);
			hScore.remove(imgScore_p2);
		}

		if(result[2]!=GreetingServiceImpl._freeID){
			hScore.add(imgScore_p3);
			s="3 : ["+result[2]+" Pts]   ";
			labelScore_p3.setText(s);
			hScore.add(labelScore_p3);
		}else {
			hScore.remove(labelScore_p3);
			hScore.remove(imgScore_p3);
		}

		if(result[3]!=GreetingServiceImpl._freeID){
			hScore.add(imgScore_p4);
			s="4 : ["+result[3]+" Pts]   ";
			labelScore_p4.setText(s);
			hScore.add(labelScore_p4);
		}else {
			hScore.remove(labelScore_p4);
			hScore.remove(imgScore_p4);
		}
	}

	public void showEndGame(String winnerPlayer){		
		// TODO : if player want re-play pop up
		DialogBox dialog = new DialogBox(true);
		dialog.setTitle("!!! The Game Is Over !!!!");
		dialog.setGlassEnabled(true);
		dialog.center();
		if(labelPlayer.getText().equals(winnerPlayer)) {
			dialog.setText("Congratulation, you are the winner! Click to close");	
		} else {
			dialog.setText("Game Over! The winner is "+winnerPlayer+"...");
		}
		dialog.show();
		//dialog.showRelativeTo(vPanel);
	}

	private void drawCookie(int x, int y){
		_context.drawImage(imgCookie, x*intervalx,y*intervaly,sizeImg,sizeImg);
	}

	private void drawMine(int x, int y){
		_context.drawImage(imgMine, x*intervalx, y*intervaly,sizeImg,sizeImg);
	}

	private void drawPlayer(int nID, int x, int y){
		switch(nID){
		case 1:
			_context.drawImage(imgPlayer1, x*intervalx, y*intervaly,sizeImg,sizeImg);
			break;
		case 2:
			_context.drawImage(imgPlayer2, x*intervalx, y*intervaly,sizeImg,sizeImg);
			break;
		case 3:
			_context.drawImage(imgPlayer3, x*intervalx, y*intervaly,sizeImg,sizeImg);
			break;
		case 4:
			_context.drawImage(imgPlayer4, x*intervalx, y*intervaly,sizeImg,sizeImg);
			break;
		}
	}

	public void addNewPlayer(byte nID, int[] coord) {
		drawPlayer(nID, coord[0],coord[1]);	
		_controle.getScore();
		// TODO : Animation new player
		//_canvas.setStyleName("changeState");

	}

	public void removeAplayer(byte id, int[] coord) {
		_controle.getGrid();
		// TODO Auto-generated method stub
		
	}
}
