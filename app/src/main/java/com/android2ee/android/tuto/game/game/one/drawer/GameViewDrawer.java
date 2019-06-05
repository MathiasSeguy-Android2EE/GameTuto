/**<ul>
 * <li>GameTuto1</li>
 * <li>com.android2ee.android.tuto.game.game.one</li>
 * <li>30 nov. 2011</li>
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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

import com.android2ee.android.tuto.game.game.one.R;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class GameViewDrawer {
	/******************************************************************************************/
	/** Canvas and paint **************************************************************************/
	/******************************************************************************************/

	/**
	 * The surface holder.
	 */
	private SurfaceHolder holder;

	/**
	 * The black paint.
	 */
	private Paint blackPaint;

	/**
	 * The context.
	 */
	private Context context;

	/**
	 * The tiles.
	 */
	private Bitmap tiles;
	/**
	 * The ball bitmap.
	 */
	private Bitmap ball;

	/******************************************************************************************/
	/** Manager **************************************************************************/
	/******************************************************************************************/
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
	private BackForeMapping mapperBF;

	/**
	 * The dst rectangle de scaleX by scaleY
	 */
	private Rect dst;

	
	
	/**
	 * The initialized boolean
	 */
	boolean init = false;

	/**
	 * Constructor.
	 * 
	 * @param holder
	 *            The surface holder
	 * @param context
	 *            The context
	 */
	public GameViewDrawer(SurfaceHolder holder, Context context) {
		Log.w("GameViewDrawer", "GameViewDrawer Constructor called");
		this.holder = holder;
		this.context = context;
		this.blackPaint = new Paint();
		this.blackPaint.setARGB(255, 0, 0, 0);
		
		background=new BackgroundManager();
		foreground=new ForegroundManager();
		mapperBF=new BackForeMapping(background,foreground);
		
		this.tiles = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.background4);
//		this.tiles = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.tiles);
		this.ball = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ball64);
		this.dst = new Rect(0, 0, 16, 16);
	}

	/**
	 * Game main loop.
	 */
	public void redraw() {
		// long start = System.currentTimeMillis();
		// long count = 0;
//		Log.d("mg", "Game thread started");

		Canvas canvas = null;
		try {
			canvas = holder.lockCanvas(null);
			synchronized (this.holder) {
				draw(canvas);
			}
			// count++;
		} finally {
			if (canvas != null) {
				holder.unlockCanvasAndPost(canvas);
			}
		}

		long end = System.currentTimeMillis();
		// this.fps = (1000 * count) / (end - start);
		// Log.d("mg", "FPS = [" + this.fps + "], count = [" + count
		// + "], duration = [" + (end - start) + "]");
		// Log.d("mg", "Game thread ended");
	}

	/**
	 * Draw bitmaps.
	 * 
	 * @param canvas
	 *            The canvas
	 */
	private void draw(Canvas canvas) {

		if (!init) {
			background.init();
			foreground.init(canvas.getWidth(),canvas.getHeight());
			mapperBF.init();
			init = true;

		}
		
		dst.top = dst.left = 0;
		dst.bottom = dst.right = 16;
		int scaleX=mapperBF.getScaleX();
		int scaleY=mapperBF.getScaleY();
		// desiner le fond d'écran
		for (int lineNumber = 0; lineNumber < background.getLevelHeight(); lineNumber++) {
			// on récupère le n° ligne du level
//			int[] line = current[lineNumber];
			// Log.w("", "line["+y+"]="+current[y]);
			// le rectangle etant de 16*16, la distance du haut est bien 16*y
			dst.top = lineNumber * scaleY;
			// le bas est donc top+16
			dst.bottom = dst.top + scaleY;
			// par contre on initialise au premier rectangle
			dst.left = 0;
			dst.right = scaleX;
			// maintenant on parcours la ligne, et pour chaque valeur de la matrice on recupere sa
			// valeur dans le tileset
			for (int colNumber = 0; colNumber < background.getLevelWidth(); colNumber++) {
//				int tile = line[rowNumber];
				// Log.w("", "tile["+y+","+x+"]="+tile);
				
				// //la source dans l'image est 16+1 (marge) *le numero de tile
				// src.top = (tile+1) * 17;
				// //son bord droit n'est autre que 16 pixel plus loins
				// src.bottom = src.top + 16;
				canvas.drawBitmap(this.tiles, background.getSrcRectangle(colNumber, lineNumber), dst, this.blackPaint);
				// on avance la destination
				dst.left += scaleX;
				dst.right += scaleX;
			}
		}
		

		final int x = Math.round(foreground.getX() - foreground.getFgdObjtW2());

		final int y = Math.round(foreground.getY()  - foreground.getFgdObjH2());
		dst.left = x;
		dst.right = x + 2*foreground.getFgdObjtW2();
		dst.top = y;
		dst.bottom = y + 2*foreground.getFgdObjH2();
		canvas.drawBitmap(this.ball, foreground.getSrcRectangle(0, 0), dst, this.blackPaint);

	}

	public void setXY(float x, float y) {
		foreground.setXY(x, y);
	}
	public void setXYAcceleration(float x, float y) {
		foreground.setXYAcceleration(x, y);
	}

	
}
