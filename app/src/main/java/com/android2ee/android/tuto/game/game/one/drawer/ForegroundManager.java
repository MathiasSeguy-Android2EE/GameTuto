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

import com.android2ee.android.tuto.game.game.one.math.AffineHelper;

import android.graphics.Rect;
import android.util.Log;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class ForegroundManager {

	/******************************************************************************************/
	/** Attribute **************************************************************************/
	/******************************************************************************************/

	/**
	 * The x value of the ball.
	 */
	private float x = 2;

	/**
	 * The y value of the ball.
	 */
	private float y = 2;
	/**
	 * canvas's width
	 */
	private int width;
	/**
	 * canvas' height
	 */
	private int height;
	/**
	 * Ball Radius
	 */
	private static final int BR = 8;
	/**
	 * The width/2 of the foreground object in the screen
	 */
	private int fgdObjtW2 = BR;
	/**
	 * The height/2 of the foreground object in the screen
	 */
	private int fgdObjH2 = BR;

	/**
	 * The rectangle of the foreground image in the tilseset
	 */
	private Rect src;
	/**
	 * The attributes to manage points' trajectory
	 */
	private float vx = 0, vy = 0;
	private long t = 0, dt;
	float xAcceleration, yAcceleration;
	int directionX, directionY;
	int[] coordinate = new int[] { 0, 0 };
	/**
	 * 
	 */
	private BackForeMapping mapperBF;

	/******************************************************************************************/
	/** Constructors and initialisation ******************************************************/
	/******************************************************************************************/

	/**
	 * 
	 */
	public ForegroundManager() {
		super();
		src = new Rect();
	}

	/**
	 * 
	 */
	public void init(int width, int height) {
		this.width = width;
		this.height = height;
		Log.w("Foreground", "Init Foreground width: " + width + ", height: " + height);
	}

	/******************************************************************************************/
	/** Trajectory manager **************************************************************************/
	/******************************************************************************************/

	public void setXYAcceleration(float x, float y) {
		xAcceleration = x;
		yAcceleration = y;
		calculateXY();
	}

	float deltaX, deltaY;

	private void calculateXY() {
		float xTemp = 0, yTemp = 0;
		long t0 = System.nanoTime();
		if (t == 0) {
			t = t0;
		} else {
			dt = t0 - t;
			int billion = 100000000;
			float dts = ((float) dt) / billion;
			if (dt == 0) {
				// Log.e("tag", "dt=0");
			} else {
				t = t0;
				vx = vx - xAcceleration * dts * 10;
				vy = vy + yAcceleration * dts * 10;
				xTemp = x + (vx * dts - (xAcceleration * dts * dts / 2)) / 50;
				yTemp = y + (vy * dts - (yAcceleration * dts * dts / 2)) / 50;
				deltaX = (vx * dts - (xAcceleration * dts * dts / 2)) / 50;
				deltaY = (vy * dts - (yAcceleration * dts * dts / 2)) / 50;
				directionX = (int) Math.signum(vx);
				directionY = (int) Math.signum(vy);
				// directionX = (directionX == 0) ? 1 : directionX;
				// directionY = (directionY == 0) ? 1 : directionY;
				// xTemp = x - xAcceleration * dts;
				// yTemp = y + yAcceleration * dts;
//				setXY(xTemp, yTemp);
				setInBoundXY(xTemp, yTemp);
				// x = x + (vx * dts - (xAcceleration * dts * dts / 2)) / 100;
				// y = y + (vy * dts - (yAcceleration * dts * dts / 2)) / 100;
			}
			Log.d("tag", "(x, y)=(" + x + "," + y + ")" + "width : " + width + ", height" + height);
			Log.d("tag", "(vx, vy)=(" + vx + "," + vy + ")" + "directionX : " + directionX + ", directionY"
					+ directionY);
		}
	}

	/**
	 * @param x
	 * @param y
	 */
	public void setXY(float x, float y) {
		// The coordinate system (according to the screen landscape)
		// (0,0)------------------->x
		// |
		// | TL---TR
		// | | ... |
		// | BL---BR
		// y
		// Now assign the step on each axes
		float deltaMax = Math.max(Math.abs(deltaX), Math.abs(deltaY));
		// a step is the delta/deltaMax
		int stepLenghtX = (int)Math.floor( deltaX / deltaMax), stepLenghtY = (int)Math.floor(deltaY / deltaMax);
		Log.e("FGM", "deltaMax " + deltaMax + ", stepLenghtX " + stepLenghtX + ", stepLenghtY " + stepLenghtY);
		// Now The step for x
		int stepX = 0, stepY = 0;
		// Assign the TopLeft corner coordinates
		float tlXi = this.x - BR;
		float tlyi = this.y - BR;
		// Assign the TopLRight corner coordinates
		float trXi = this.x + BR;
		float tryi = this.y - BR;
		// Assign the BottomLeft corner coordinates
		float blXi = this.x - BR;
		float blyi = this.y + BR;
		// Assign the BottomRight corner coordinates
		float brXi = this.x + BR;
		float bryi = this.y + BR;
		// the last coordinate that didn't hit a wall
		int lastXOk = (int) this.x, lastYOk = (int) this.y;
		// Now assign the boolean to know which corner is in the wall
		boolean tlInW = false, trInW = false, blInW = false, brInW = false;
		// and to know which side hits the wall
		// wallFound= both side hits the well, wallFoundX=true means the object hits the wall on a
		// wall parallel to the X-Axis
		// wallFoundY=true means the object hits the wall on a wall parallel to the Y-Axis
		boolean wallFound = false, wallFoundX = false, wallFoundY = false;
		int correctionX = 0, correctionY = 0;
		// then we can begin to make move the point until it touch a wall
		for (int step = 0; ((step <= deltaMax)&& (!wallFound)); step++) {// && (!wallFound)
			
			Log.e("FGM" + step, "New for loop");
			// check if one corner is in the wall:
			tlInW = (mapperBF.getLevelFromScreen(tlXi, tlyi) != 0);
			trInW = (mapperBF.getLevelFromScreen(trXi, tryi) != 0);
			blInW = (mapperBF.getLevelFromScreen(blXi, blyi) != 0);
			brInW = (mapperBF.getLevelFromScreen(brXi, bryi) != 0);
			Log.e("FGM" + step, "tlInW " + tlInW + ", trInW " + trInW + ", blInW " + blInW + ", brInW " + brInW);
		
			if (brInW || blInW || tlInW || trInW) {
				Log.e("FGM" + step, "Wall NOT found Corner found ");
				// Don't know what to do
				wallFound=true;
				lastXOk = (int) (lastXOk- stepLenghtX);
				lastYOk = (int) (lastYOk-stepLenghtY);
			} else {
				Log.e("FGM" + step, "Wall NOT found Corner not Found Moving");
				lastXOk = (int) (lastXOk+ stepLenghtX);
				lastYOk = (int) (lastYOk+stepLenghtY);
				stepX++;
				stepY++;
				// then increment
				tlXi = (int) (tlXi + stepLenghtX);
				tlyi = (int) (tlyi + stepLenghtY);
				trXi = (int) (trXi + stepLenghtX);
				tryi = (int) (tryi + stepLenghtY);
				blXi = (int) (blXi + stepLenghtX);
				blyi = (int) (blyi + stepLenghtY);
				brXi = (int) (brXi + stepLenghtX);
				bryi = (int) (bryi + stepLenghtY);
			}
			Log.e("FGM" + step, "lastXOk : "+lastXOk+" lastYOk: "+lastYOk);
		}
		

		Log.e("FGM", "The for is finished");
		setInBoundXY(lastXOk, lastYOk);

//		if (wallFound) {
//			Log.e("FGM", "Ends with Wall found ");
//			// so wall found
//			// set the coordinates
//			// setInBoundXY((tlXi+trXi)/2, (tlyi+blyi)/2);
//			setInBoundXY(lastXOk, lastYOk);
//			// setInBoundXY(this.x + (stepX - 1) * stepLenghtX, this.y + (stepY - 1) * stepLenghtY);
////			vx = 0 * vx;
////			vy = 0 * vy;
//		}  else{
//			Log.e("FGM", "Ends with Wall Not found ");
//			// nothing happens, no wall hits
//			setInBoundXY(x, y);
//		}

	}

	/**
	 * Old version with a bug
	 * 
	 * @param x
	 * @param y
	 */
	public void setXYV1(float x, float y) {
		// The coordinate system (according to the screen landscape)
		// (0,0)------------------->x
		// |
		// | TL---TR
		// | | ... |
		// | BL---BR
		// y
		// Now assign the step on each axes
		float deltaMax = Math.max(Math.abs(deltaX), Math.abs(deltaY));
		// a step is the delta/deltaMax
		float stepLenghtX = deltaX / deltaMax, stepLenghtY = deltaY / deltaMax;
		Log.e("FGM", "deltaMax " + deltaMax + ", stepLenghtX " + stepLenghtX + ", stepLenghtY " + stepLenghtY);
		// Now The step for x
		int stepX = 0, stepY = 0;
		// Assign the TopLeft corner coordinates
		float tlXi = this.x - BR;
		float tlyi = this.y - BR;
		// Assign the TopLRight corner coordinates
		float trXi = this.x + BR;
		float tryi = this.y - BR;
		// Assign the BottomLeft corner coordinates
		float blXi = this.x - BR;
		float blyi = this.y + BR;
		// Assign the BottomRight corner coordinates
		float brXi = this.x + BR;
		float bryi = this.y + BR;
		// the last coordinate that didn't hit a wall
		int lastXOk = (int) this.x, lastYOk = (int) this.y;
		// Now assign the boolean to know which corner is in the wall
		boolean tlInW = false, trInW = false, blInW = false, brInW = false;
		// and to know which side hits the wall
		// wallFound= both side hits the well, wallFoundX=true means the object hits the wall on a
		// wall parallel to the X-Axis
		// wallFoundY=true means the object hits the wall on a wall parallel to the Y-Axis
		boolean wallFound = false, wallFoundX = false, wallFoundY = false;
		// then we can begin to make move the point until it touch a wall
		for (int step = 0; ((step <= deltaMax) && (!wallFound)); step++) {

			Log.e("FGM" + step, "New for loop");
			// check if one corner is in the wall:
			tlInW = (mapperBF.getLevelFromScreen(tlXi, tlyi) != 0);
			trInW = (mapperBF.getLevelFromScreen(trXi, tryi) != 0);
			blInW = (mapperBF.getLevelFromScreen(blXi, blyi) != 0);
			brInW = (mapperBF.getLevelFromScreen(brXi, bryi) != 0);
			Log.e("FGM" + step, "tlInW " + tlInW + ", trInW " + trInW + ", blInW " + blInW + ", brInW " + brInW);
			// then analyze the in the wall booleans
			if ((tlInW && trInW) || (blInW && brInW)) {
				Log.e("FGM" + step, "Y Side hit");
				// case where the x coordinates hit the wall
				wallFoundY = true;
			}
			if ((tlInW && blInW) || (trInW && brInW)) {
				Log.e("FGM" + step, "X Side hit");
				// case where the y coordinates hit the wall
				wallFoundX = true;
			}
			if (!(wallFoundX || wallFoundY)) {
				// case where just a piece of the side of the object hit the wall
				if (tlInW) {
					Log.e("FGM" + step, "TL Side hit");
					// check which side hits the wall
					if (mapperBF.getLevelFromScreen(tlXi + (BR / 2), tlyi) != 0) {
						Log.e("FGM" + step, "TL X Side hit");
						// the it's the the top side
						wallFoundY = true;
					}
					if (mapperBF.getLevelFromScreen(tlXi, tlyi + (BR / 2)) != 0) {
						Log.e("FGM" + step, "TL Y Side hit");
						// case where the y coordinates hit the wall
						wallFoundX = true;
					}
				}
				if (trInW) {
					Log.e("FGM" + step, "TR Side hit");
					// check which side hits the wall
					if (mapperBF.getLevelFromScreen(trXi - (BR / 2), tryi) != 0) {
						Log.e("FGM" + step, "TR X Side hit");
						// the it's the the top side
						wallFoundY = true;
					}
					if (mapperBF.getLevelFromScreen(trXi, tryi + (BR / 2)) != 0) {
						Log.e("FGM" + step, "TR Y Side hit");
						// case where the y coordinates hit the wall
						wallFoundX = true;
					}

				}
				if (blInW) {
					Log.e("FGM" + step, "BL Side hit");
					// check which side hits the wall
					if (mapperBF.getLevelFromScreen(blXi + (BR / 2), blyi) != 0) {
						Log.e("FGM" + step, "BL X Side hit");
						// the it's the the top side
						wallFoundY = true;
					}
					if (mapperBF.getLevelFromScreen(blXi, blyi - (BR / 2)) != 0) {
						Log.e("FGM" + step, "BL Y Side hit");
						// the it's the the left side
						// case where the y coordinates hit the wall
						wallFoundX = true;
					}
				}
				if (brInW) {
					Log.e("FGM" + step, "BR Side hit");
					// check which side hits the wall
					if (mapperBF.getLevelFromScreen(brXi - (BR / 2), bryi) != 0) {
						Log.e("FGM" + step, "BR X Side hit");
						// the it's the the top side
						wallFoundY = true;
					}
					if (mapperBF.getLevelFromScreen(brXi, bryi - (BR / 2)) != 0) {
						Log.e("FGM" + step, "BR Y Side hit");
						// case where the y coordinates hit the wall
						wallFoundX = true;
					}
				}
			}
			wallFound = wallFoundX && wallFoundY;

			// Increment elements
			if (wallFound) {
				Log.e("FGM" + step, "Wall found");
				// we can move only if it release the object
				if ((blInW && (stepLenghtX > 0)) || (tlInW && (stepLenghtX > 0)) || (trInW && (stepLenghtX < 0))
						|| (brInW && (stepLenghtX < 0))) {
					// then increment X
					tlXi = (int) (tlXi + stepLenghtX);
					trXi = (int) (trXi + stepLenghtX);
					blXi = (int) (blXi + stepLenghtX);
					brXi = (int) (brXi + stepLenghtX);
				}
				// we can move only if it release the object
				if ((tlInW && (stepLenghtY > 0)) || (brInW && (stepLenghtY < 0)) || (trInW && (stepLenghtY > 0))
						|| (blInW && (stepLenghtY < 0))) {
					// then increment Y
					tlyi = (int) (tlyi + stepLenghtY);
					tryi = (int) (tryi + stepLenghtY);
					blyi = (int) (blyi + stepLenghtY);
					bryi = (int) (bryi + stepLenghtY);
				}
				// then increment
			} else if (wallFoundY) {
				Log.e("FGM" + step, "Wall found Y");
				lastXOk = (int) (this.x + step * stepLenghtX);
				stepX++;
				// then increment X
				tlXi = (int) (tlXi + stepLenghtX);
				trXi = (int) (trXi + stepLenghtX);
				blXi = (int) (blXi + stepLenghtX);
				brXi = (int) (brXi + stepLenghtX);
				// Increment Y iff it's in the good direction:
				if ((tlInW && (stepLenghtY > 0)) || (brInW && (stepLenghtY < 0)) || (trInW && (stepLenghtY > 0))
						|| (blInW && (stepLenghtY < 0))) {
					// then increment Y
					tlyi = (int) (tlyi + stepLenghtY);
					tryi = (int) (tryi + stepLenghtY);
					blyi = (int) (blyi + stepLenghtY);
					bryi = (int) (bryi + stepLenghtY);
				}

			} else if (wallFoundX) {
				Log.e("FGM" + step, "Wall found X");
				lastYOk = (int) (this.y + step * stepLenghtY);
				stepY++;
				// then increment Y
				tlyi = (int) (tlyi + stepLenghtY);
				tryi = (int) (tryi + stepLenghtY);
				blyi = (int) (blyi + stepLenghtY);
				bryi = (int) (bryi + stepLenghtY);
				// increment if and only if it's in the good direction
				if ((blInW && (stepLenghtX > 0)) || (tlInW && (stepLenghtX > 0)) || (trInW && (stepLenghtX < 0))
						|| (brInW && (stepLenghtX < 0))) {
					// then increment X
					tlXi = (int) (tlXi + stepLenghtX);
					trXi = (int) (trXi + stepLenghtX);
					blXi = (int) (blXi + stepLenghtX);
					brXi = (int) (brXi + stepLenghtX);
				}
			} else {
				Log.e("FGM" + step, "Wall NOT found ");
				if (brInW || blInW || tlInW || trInW) {
					// Don't know what to do
				} else {

					lastXOk = (int) (this.x + step * stepLenghtX);
					lastYOk = (int) (this.y + step * stepLenghtY);
					stepX++;
					stepY++;
					// then increment
					tlXi = (int) (tlXi + stepLenghtX);
					tlyi = (int) (tlyi + stepLenghtY);
					trXi = (int) (trXi + stepLenghtX);
					tryi = (int) (tryi + stepLenghtY);
					blXi = (int) (blXi + stepLenghtX);
					blyi = (int) (blyi + stepLenghtY);
					brXi = (int) (brXi + stepLenghtX);
					bryi = (int) (bryi + stepLenghtY);
				}
			}
		}

		Log.e("FGM", "The for is finished");

		if (wallFound) {
			// so wall found
			// set the coordinates
			// setInBoundXY((tlXi+trXi)/2, (tlyi+blyi)/2);
			setInBoundXY(lastXOk, lastYOk);
			// setInBoundXY(this.x + (stepX - 1) * stepLenghtX, this.y + (stepY - 1) * stepLenghtY);
			vx = 0 * vx;
			vy = 0 * vy;
		} else if (wallFoundX) {
			Log.e("FGM", "Ends with Wall found X");
			// setInBoundXY((tlXi+trXi)/2, (tlyi+blyi)/2);
			setInBoundXY(lastXOk, lastYOk);
			// set the speed
			vx = 0 * vx;
			// and the coordinates
			setInBoundXY(this.x + (stepX - 1) * stepLenghtX, this.y + (stepY) * stepLenghtY);
		} else if (wallFoundY) {
			Log.e("FGM", "Ends with Wall found Y");
			// setInBoundXY((tlXi+trXi)/2, (tlyi+blyi)/2);
			setInBoundXY(lastXOk, lastYOk);
			// set the speed
			vy = 0 * vy;
			// and the coordinates
			// setInBoundXY(this.x + (stepX) * stepLenghtX, this.y + (stepY - 1) * stepLenghtY);
		} else if (brInW || blInW || tlInW || trInW) {
			setInBoundXY(lastXOk, lastYOk);
		} else if (!wallFound) {
			Log.e("FGM", "Ends with Wall Not found ");
			// nothing happens, no wall hits
			setInBoundXY(x, y);
		}

	}

	private void setInBoundXY(float x, float y) {

		Log.e("ForegroundManager", "this.x " + this.x + ", this.y " + this.y + " => x " + x + ", y " + y);
		if ((x >= 0) && (x <= width)) {
			this.x = (int) x;
		} else {
			vx = -1 * vx;
		}
		if ((y >= 0) && (y <= height)) {
			this.y = (int) y;
		} else {
			vy = -1 * vy;
		}
	}

	/******************************************************************************************/
	/** Methods Assessors **************************************************************************/
	/******************************************************************************************/

	/**
	 * Return the rectangle of the tilesSet associated to foreground image
	 * 
	 * @param row
	 * @param line
	 * @return the rectangle of the tilesSet associated to foreground image
	 */
	public Rect getSrcRectangle(int row, int line) {
		// this is the ball
		src.top = src.left = 0;
		src.bottom = 64;
		src.right = 64;
		return src;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param mapperBF
	 *            the mapperBF to set
	 */
	public void setMapperBF(BackForeMapping mapperBF) {
		this.mapperBF = mapperBF;
	}

	/**
	 * @return the fgdObjtW2
	 */
	public int getFgdObjtW2() {
		return fgdObjtW2;
	}

	/**
	 * @return the fgdObjeH2
	 */
	public int getFgdObjH2() {
		return fgdObjH2;
	}

}
