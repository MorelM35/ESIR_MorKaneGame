package edu.game.server;

import java.util.Random;

import edu.game.client.GreetingService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
	private int _gridx = 20;
	private int _gridy = 20;
	private int _nbCookies = 50;
	private int[][] _grid;
		
	public GreetingServiceImpl() {
		_grid=new int[_gridx][_gridy];
		InitGrid(_nbCookies);
	}
	
	@Override
	public boolean moveUp() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveDown() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveLeft() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveRight() {
		// TODO Auto-generated method stub
		return false;
	}
	
	// Put cookies in the grid
	private void InitGrid(int nbCookies){
		//Init empty grid
		for(int i=0;i<_gridx;i++){
			for(int j=0;j<_gridy;j++){
				_grid[i][j]=0;
			}			
		}
		
		// TODO : Find Optimisation
		
		// add cookies in the grid
		Random r = new Random();
		for(int i=0;i<nbCookies;i++){
			int x = r.nextInt(_gridx);	
			int y = r.nextInt(_gridy);
			//System.out.println("Cookie "+x+":"+y);
			_grid[x][y]=1;
		}
	}

	@Override
	public int[][] getGrid() {
		return _grid;
	}
}
