package edu.game.server;

import edu.game.client.GreetingService;
import edu.game.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
	private int _gridx = 20;
	private int _gridy = 20;
	private int[][] _grid;
		
	public GreetingServiceImpl() {
		_grid=new int[_gridx][_gridy];
		InitGrid();
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
	private void InitGrid(){
		//TODO : do random init
		for(int i=0;i<_gridx;i++){
			for(int j=0;j<_gridy;j++){
				_grid[i][j]=0;
			}			
		}
		_grid[0][4]=1;
		_grid[1][10]=1;
		_grid[18][5]=1;
		_grid[12][12]=1;
		_grid[19][3]=1;
		_grid[6][2]=1;
		_grid[4][7]=1;
	}

	@Override
	public int[][] getGrid() {
		return _grid;
	}
}
