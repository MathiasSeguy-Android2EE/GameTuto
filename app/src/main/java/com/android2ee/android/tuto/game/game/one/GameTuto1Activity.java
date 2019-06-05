package com.android2ee.android.tuto.game.game.one;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.android2ee.android.tuto.game.game.one.R;

public class GameTuto1Activity extends Activity implements SensorEventListener {
	GameView view;
	/** * The sensor manager */
	SensorManager sensorManager;
	/**
	 * The accelerometer
	 */
	Sensor accelerometer;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w("GameTuto1Activity", "GameTuto1Activity Constructor called");
		setContentView(R.layout.main);
		view = (GameView) findViewById(R.id.screen);
		// Then manage the sensors and listen for changes
		// Instanciate the SensorManager
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// instanciate the accelerometer
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	@Override
	protected void onDestroy() {
		Log.w("GameTuto1Activity", "GameTuto1Activity onDestroy called");
		view.isThreadRunnning.set(false);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.w("GameTuto1Activity", "GameTuto1Activity onPause called");
		// unregister every body
		sensorManager.unregisterListener(this, accelerometer);
		// and don't forget to stop the thread that redraw the xyAccelerationView
		view.isThreadPausing.set(true);
		super.onPause();
	}

	@Override
	protected void onResume() {
		Log.w("GameTuto1Activity", "GameTuto1Activity onResume called");
		/*
		 * It is not necessary to get accelerometer events at a very high
		 * rate, by using a slower rate (SENSOR_DELAY_UI), we get an
		 * automatic low-pass filter, which "extracts" the gravity component
		 * of the acceleration. As an added benefit, we use less power and
		 * CPU resources.
		 */
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		// and don't forget to relaunch the thread that redraw the xyAccelerationView
		view.isThreadPausing.set(false);
		super.onResume();
	}

	/******************************************************************************************/
	/** Sensor Management **************************************************************************/
	/******************************************************************************************/


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
//		Due to orientation of the device (landscape mode) the x,y correction
		//should be reto meier reorientation
//		xAcceleration = -event.values[1];
//		yAcceleration = event.values[0];
		view.setXYAcceleration(-event.values[1],event.values[0]);
	}

	
}