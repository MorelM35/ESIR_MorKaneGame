package edu.game.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class BackUp {

	/**
	 * 
	 * @param session
	 */
	public synchronized static void saveClient(Player client){
		try {			
			String current = new java.io.File( "." ).getCanonicalPath();
			File file = new File(current+"/client"+client.get_ID()+".data");
			if(!file.exists()) file.createNewFile();

			FileWriter writer = new FileWriter(file,false);
			writer.append(client.toString()+"\n");
			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public static void resetBackUp(){
		try{
			String current = new java.io.File( "." ).getCanonicalPath();

			for(int i=0;i<GreetingServiceImpl._nbMaxPlayers;i++){
				File file = new File(current+"/client"+(i+1)+".data");
				if(file.exists()){
					file.delete();
				}
			}

			File file = new File(current+"/grid.data");
			if(file.exists()){
				file.delete();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param grid
	 */
	public synchronized static void saveGrid(byte[][] grid){
		try{
			String current = new java.io.File( "." ).getCanonicalPath();
			File file = new File(current+"/grid.data");
			if(!file.exists()) file.createNewFile();

			FileWriter writer = new FileWriter(file,false);
			for(byte[] line : grid){
				for(byte col : line){
					writer.append(""+col+"\t");
				}
				writer.append("\n");
			}
			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @return
	 */
	public static byte[][] restoreGrid(){
		byte[][] myGrid =new byte[GreetingServiceImpl._gridx][GreetingServiceImpl._gridy]; ;
		try{
			String current = new java.io.File( "." ).getCanonicalPath();			
			BufferedReader in = new BufferedReader(new FileReader(current+"/grid.data"));
			String line;
			int gridx = 0;
			while ((line = in.readLine()) != null)
			{
				String[] caract = line.split("\t");
				for(int i=0;i<caract.length;i++){
					myGrid[gridx][i]=Byte.parseByte(caract[i]);
				}
				gridx++;
			}
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return myGrid;
	}

	public static HashMap<Byte,Player> restoreMapPlayer(){
		HashMap<Byte,Player> map = new HashMap<Byte,Player>();
		try{
			String current = new java.io.File( "." ).getCanonicalPath();
			System.out.println("path grid : "+current+"/grid.data");

			for(int i=0;i<GreetingServiceImpl._nbMaxPlayers;i++){
				BufferedReader in = new BufferedReader(new FileReader(current+"/client"+(i+1)+".data"));
				String line;
				while ((line = in.readLine()) != null)
				{
					String [] features = line.split(";");
					System.out.println("\nadd player "+features[0]+" !"+features[1]);
					// Init coord
					String[] sCoord = features[1].split("_");
					System.out.println("size scoord "+sCoord.length);
					//for(int k = 0;k<sCoord.length;k++) System.out.println(k +" : "+sCoord[k]);
					System.out.println("* coord ["+sCoord[0]+"]["+sCoord[1]+"]");
					int[] coord = new int[2];
					coord[0] = Integer.parseInt(sCoord[0]);
					coord[1] = Integer.parseInt(sCoord[1]);

					Player player = new Player(Byte.parseByte(features[0]), coord, features[2], Short.parseShort(features[3]));
					System.out.println("* score= "+features[3]);
					map.put(player.get_ID(), player);
				}
				in.close();
				//System.out.println("MapSize : "+map.size());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}


	/**
	 * 
	 * @return
	 */
	public static boolean isCrashed(){
		try{
			String current = new java.io.File( "." ).getCanonicalPath();
			File fileGrid = new File(current+"/grid.data");
			if(fileGrid.exists()){
				return true;
			} else return false;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
