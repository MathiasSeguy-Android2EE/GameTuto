/**<ul>
 * <li>GameTuto1</li>
 * <li>com.android2ee.android.tuto.game.game.one.drawer</li>
 * <li>2 déc. 2011</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 *
 /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br> 
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 *  Belongs to <strong>Mathias Seguy</strong></br>
 ****************************************************************************************************************</br>
 * This code is free for any usage but can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * 
 * *****************************************************************************************************************</br>
 *  Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 *  Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br> 
 *  Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 *  <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */
package com.android2ee.android.tuto.game.game.one.drawer;

import android.util.Log;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class BackForeMapping {
	// un rectangle dans le bitmap est de 16*(16 + 1) de marge
	// il faut donc faire que dans la destination la largeur et la hauteur correspondent
	// donc scaleX=taille d'une ligne du level/canvas.width
	// et scaleY=taille d'une colonne du level/canvas.height
	/**
	 * Taille en pixel de la largeur d'un carré du level
	 */
	private int scaleX;
	/**
	 * Taille en pixel de la hauteur d'un carré du level
	 */
	private int scaleY;
	/**
		 * 
		 */
	private BackgroundManager background;
	/**
		 * 
		 */
	private ForegroundManager foreground;

	/**
	 * 
	 */
	public BackForeMapping(BackgroundManager back, ForegroundManager fore) {
		super();
		background = back;
		background.setMapperBF(this);
		foreground = fore;
		foreground.setMapperBF(this);
	}

	/**
	 * 
	 */
	public void init() {
		scaleX = foreground.getWidth() / background.getLevelWidth();
		if((foreground.getWidth() % background.getLevelWidth())!=0) {
			scaleX++;
		}
		scaleY = foreground.getHeight() / background.getLevelHeight();
		if((foreground.getHeight() % background.getLevelHeight()!=0)) {
			scaleY++;
		}
		Log.w("BackForeMapping", "Init BackForeMapping scaleX: "+scaleX+", height: "+scaleY);
	}

	/**
	 * @return the scaleX
	 */
	public int getScaleX() {
		return scaleX;
	}

	/**
	 * @return the scaleY
	 */
	public int getScaleY() {
		return scaleY;
	}
	
	
	/**
	 * Given a point in the screen with (x,y) coordonates, return the associated level value
	 * @param x
	 * @param y
	 * @return the value of the level[i][j] according to the (x,y) position of the screen
	 */
	public int getLevelFromScreen(float x, float y) {
		int rowNumber=(int) (y/scaleY);
		int colNumber=(int) (x/scaleX);
		return background.getLevelAt(rowNumber, colNumber);
		
	}
	/**
	 * Given a point in the screen with (x,y) coordonates, return the associated level value
	 * @param x
	 * @param y
	 * @return the value of the level[i][j] according to the (x,y) position of the screen
	 */
	public int getLevelFromScreen(float x, float y,int[]position) {
		int rowNumber=(int) (y/scaleY);
		int colNumber=(int) (x/scaleX);
		position[0]=rowNumber;
		position[1]=colNumber;
		return background.getLevelAt(rowNumber, colNumber);
		
	}
	/**
	 * Given a point in the screen with (x,y) coordonates, return the associated level value
	 * @param x
	 * @param y
	 * @return the value of the level[i][j] according to the (x,y) position of the screen
	 */
	public int getLevelAt(int row, int column) {
		return background.getLevelAt(row, column);
		
	}
	/**
	 * Given a point in the screen with (x,y) coordonates, return the associated row and column number in the level matrix 
	 * @param x
	 * @param y
	 * @return the value of the [i][j] according to the (x,y) position of the screen
	 */
	public int[] getLevelCoordinateFromScreen(float x, float y) {
		int[]coordinate=new int[2];
		coordinate[0]=(int) (y/scaleY);
		coordinate[1]=(int) (x/scaleX);
		return coordinate;
		
	}
	
	/**
	 * Given a (row,col) coordinate in the matrix level return the (x,y) coordinate in the screen coordinate
	 * @param row
	 * @param col
	 * @return the (x,y) coordinate in the screen coordinate
	 */
	public float[] getScreenCoordinateFromLevel(int row, int col) {
		float[] coord=new float[2];
		coord[0]=col*scaleX+scaleX/2;
		coord[1]=row*scaleY+scaleY/2;
		return coord;
	}

}
