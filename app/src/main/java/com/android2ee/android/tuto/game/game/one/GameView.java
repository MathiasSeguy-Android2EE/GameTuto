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
package com.android2ee.android.tuto.game.game.one;

import java.util.concurrent.atomic.AtomicBoolean;

import com.android2ee.android.tuto.game.game.one.drawer.GameViewDrawer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class GameView extends SurfaceView implements Callback{
	/**
	 * The one that redraw the canvas
	 */
	private GameViewDrawer drawer;
	/******************************************************************************************/
	/** Managing the refressing screen thread *************************************************/
	/******************************************************************************************/

	private Handler refreshingHandler;
	AtomicBoolean isThreadRunnning = new AtomicBoolean();
	AtomicBoolean isThreadPausing = new AtomicBoolean();
	/******************************************************************************************/
	/** The x and y **************************************************************************/
	/******************************************************************************************/

	private float x = -1;
	private float y = -2;
	private long fps;
	Thread backgroundThread;

	/**
	 * @param context
	 * @param attrs
	 */
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.w("GameView", "GameView Constructor called");
		fps = 0;
		SurfaceHolder holder = getHolder();
		drawer = new GameViewDrawer(holder, context);
		holder.addCallback(this);
		setFocusable(true);

		// handler definition
		refreshingHandler = new Handler() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see android.os.Handler#handleMessage(android.os.Message)
			 */
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(isThreadRunnning.get()) {
					redraw();
				}
			}
		};
		final double random=Math.random();				
		// Launching the Thread to update draw
		backgroundThread = new Thread(new Runnable() {
			
			/**
			 * The message exchanged between this thread and the handler
			 */
			Message myMessage;

			// Overriden Run method
			public void run() {
				try {
					Log.e("GameView", "GameView NewThread "+random);
					while (isThreadRunnning.get()) {
//						Log.w("GameView", "GameView isThreadRunnning true "+random);
						if (isThreadPausing.get()) {
//							Log.w("GameView", "GameView isThreadPausing true "+random);
							Thread.sleep(2000);
						} else {
//							Log.w("GameView", "GameView isThreadPausing false "+random);
							// Redraw to have 30 images by second
							Thread.sleep(1000 / 30);
							// Send the message to the handler (the
							// handler.obtainMessage is more
							// efficient that creating a message from scratch)
							// create a message, the best way is to use that
							// method:
							myMessage = refreshingHandler.obtainMessage();
							// then send the message

							refreshingHandler.sendMessage(myMessage);
						}
					}
				} catch (Throwable t) {
					// just end the background thread
				}
			}
		});
		backgroundThread.setName(""+random);
		// Initialize the threadSafe booleans
		isThreadRunnning.set(true);
		isThreadPausing.set(true);
		backgroundThread.start();
	}

	/**
	 * Handle touch tracking.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		x = event.getX(0);
		y = event.getY(0);
		drawer.setXY(this.x, this.y);
		return true;
	}

	/******************************************************************************************/
	/** Handler management **************************************************************************/
	/******************************************************************************************/
	private void redraw() {
		long start = System.currentTimeMillis();
		long count = 0;
		Log.d("mg", "Game thread started");
		drawer.redraw();
		long end = System.currentTimeMillis();
		this.fps = (1000 * count) / (end - start);
//		Log.d("mg", "FPS = [" + this.fps + "], count = [" + count + "], duration = [" + (end - start) + "]");
//		Log.d("mg", "Game thread ended");
	}

	

	public void setXYAcceleration(float xa,float ya) {
		drawer.setXYAcceleration(xa, ya);
	}
	/******************************************************************************************/
	/** The SurfaceHolder callBack implementation **************************************************************************/
	/******************************************************************************************/
	// A client may implement this interface to receive information about
	// changes to the surface.
	// When used with a SurfaceView, the Surface being held is only available
	// between calls to
	// surfaceCreated(SurfaceHolder) and The Callback is set with {@link
	// SurfaceHolder#addCallback
	// SurfaceHolder.addCallback method.

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder
	 * , int, int, int)
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder
	 * )
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// Initialize the threadSafe booleans
		 isThreadRunnning.set(true);
		 isThreadPausing.set(false);
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.
	 * SurfaceHolder)
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isThreadRunnning.set(false);
		boolean alive = true;
		while (alive) {
			try {
				backgroundThread.join();
				alive = false;
			} catch (InterruptedException e) {
			}
		}
	}

}
