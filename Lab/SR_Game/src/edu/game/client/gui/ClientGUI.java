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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.game.client.ClientImpl;
import edu.game.server.GreetingServiceImpl;


public class ClientGUI extends Composite  {
	private ClientImpl _controle;
	private Label labelPlayer;
	private VerticalPanel vPanel = new VerticalPanel();

	private TextArea txtScore;
	private Context2d 		context;
	private ImageElement 	imgCookie;
	private ImageElement 	imgMine;
	private ImageElement 	imgPlayer1;
	private ImageElement 	imgPlayer2;
	private ImageElement 	imgPlayer3;
	private ImageElement 	imgPlayer4;
	private Image			imgCurrentPlayer;
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
	private static final String urlStartingPlot = "http://jolstatic.fr/wiki/images/ffxiv/thumb/5/54/Icone_Parquet_des_temp%C3%AAtes.png/65px-Icone_Parquet_des_temp%C3%AAtes.png";

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
		
		labelPlayer= new Label("Loading...");

		vPanel.add(imgCurrentPlayer);
		vPanel.add(labelPlayer);

		// ########## Init Canvas
		Canvas canvas = Canvas.createIfSupported();
		if(canvas==null){
			DialogBox dialog = new DialogBox(true);
			dialog.setTitle("Error");
			dialog.setText("can't continue");
			dialog.showRelativeTo(vPanel);
		}
		vPanel.add(canvas);
		canvas.setWidth(width+"pt");
		canvas.setHeight(height+"pt");
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
		context = canvas.getContext2d();


		// Init Key Handler
		canvas.addKeyDownHandler(new KeyDownHandler() {
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

		/**
		 * Version 1 : Show in TextArea
		 */
		/*
		// Init window TextArea
		txt= new TextArea();
		txt.setPixelSize(1000,380);
		txt.setAlignment(TextAlignment.CENTER);
		txt.setReadOnly(true);
		txt.setText("Loading...");
		vPanel.add(txt); 
		 */
	}

	/**
	 * Set the name of player 
	 * @param name
	 */
	public void setNameOfPlayer(byte nID){
		switch(nID){
		case -1:
			imgCurrentPlayer.setUrl(urlSpectator);
			labelPlayer.setText("The room is full !! You are a spectator (if you want try to play, press 'A')");
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
		context.clearRect(0, 0, width, height);
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
		context.stroke();
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
		dialog.setText("The winner of this game is "+winnerPlayer+"\n Congratulation!\n");
		dialog.showRelativeTo(vPanel);

	}

	private void drawCookie(int x, int y){
		context.drawImage(imgCookie, x*intervalx,y*intervaly,sizeImg,sizeImg);
	}

	private void drawMine(int x, int y){
		context.drawImage(imgMine, x*intervalx, y*intervaly,sizeImg,sizeImg);
	}

	private void drawPlayer(int nID, int x, int y){
		switch(nID){
		case 1:
			context.drawImage(imgPlayer1, x*intervalx, y*intervaly,sizeImg,sizeImg);
			break;
		case 2:
			context.drawImage(imgPlayer2, x*intervalx, y*intervaly,sizeImg,sizeImg);
			break;
		case 3:
			context.drawImage(imgPlayer3, x*intervalx, y*intervaly,sizeImg,sizeImg);
			break;
		case 4:
			context.drawImage(imgPlayer4, x*intervalx, y*intervaly,sizeImg,sizeImg);
			break;
		}
	}
}
