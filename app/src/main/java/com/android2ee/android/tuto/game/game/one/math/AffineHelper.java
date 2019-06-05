/**<ul>
 * <li>GameTuto1</li>
 * <li>com.android2ee.android.tuto.game.game.one.math</li>
 * <li>11 déc. 2011</li>
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
package com.android2ee.android.tuto.game.game.one.math;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class AffineHelper {

	/**
	 * Return the slope (pente) a of the linear equation that goes through (x0,y0) and (x1,y1)
	 * such that y=ax+b
	 * 
	 * @param x0
	 *            abscissa of the first point
	 * @param y0
	 *            ordinate of the first point
	 * @param x1
	 *            abscissa of the second point
	 * @param y1
	 *            ordinate of the second point
	 * @return the slope a of the affine such that y=ax+b
	 */
	public static float getXSlope(float x0, float y0, float x1, float y1) {
		return (x0 - x1) / (y0 - y1);
	}

	/**
	 * Return the slope (pente) a of the linear equation that goes through (x0,y0) and (x1,y1)
	 * according to y
	 * such that x=ay-b
	 * 
	 * @param x0
	 *            abscissa of the first point
	 * @param y0
	 *            ordinate of the first point
	 * @param x1
	 *            abscissa of the second point
	 * @param y1
	 *            ordinate of the second point
	 * @return the slope a of the affine such that x=ay+b
	 */
	public static float getYSlope(float x0, float y0, float x1, float y1) {
		return (y0 - y1) / (x0 - x1);
	}

	/**
	 * Return the y-intercept (b) of the linear equation that goes through (x0,y0) and (x1,y1)
	 * according to x
	 * such that y=ax+b
	 * 
	 * @param x0
	 *            abscissa of the first point
	 * @param y0
	 *            ordinate of the first point
	 * @param x1
	 *            abscissa of the second point
	 * @param y1
	 *            ordinate of the second point
	 * @return the y-intercept b of the equation y=ax+b 
	 */
	public static float getYIntercept(float x0, float y0, float x1, float y1) {
		return (y0*(y0-y1)-x0*(x0-x1))/(y0-y1);
	}
	/**
	 * Return the y-intercept (b) of the linear equation that goes through (x0,y0) and (x1,y1)
	 * according to x
	 * such that y=ax+b
	 * 
	 * @param x0
	 *            abscissa of the first point
	 * @param y0
	 *            ordinate of the first point
	 * @param x1
	 *            abscissa of the second point
	 * @param y1
	 *            ordinate of the second point
	 * @return the y-intercept b of the equation x=ay+b 
	 */
	public static float getXIntercept(float x0, float y0, float x1, float y1) {
		return (x0*(x0-x1)-y0*(y0-y1))/(y0-y1);
	}
	
	/**
	 * Return the y value of the linear equation for ax+b
	 * @param x the abscissa
	 * @param a the slope
	 * @param b the y-intercept
	 * @return y
	 */
	public static int getYFromLinear(int x, float a, float b) {
		return (int) (a*x+b);
	}
	/**
	 * Return the y value of the linear equation for ax+b
	 * @param x the abscissa
	 * @param a the slope
	 * @param b the y-intercept
	 * @return y
	 */
	public static int getXFromLinear(int y, float a, float b) {
		return (int) (a*y+b);
	}
}
