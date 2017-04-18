package sh1457.test.com.grand;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

public class GrandActivity extends AppCompatActivity implements SensorEventListener {
    private static final int threshold_x=2;
    private static final int threshold_y=2;

    SensorManager manager;
    Sensor accelerometer;

    static int counter=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grand);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        manager.unregisterListener(this, accelerometer);
    }

    @Override
    public void onResume() {
        super.onResume();
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] history = new float[2];
        int x=0, y=0;

        float xChange = history[0] - event.values[0];
        float yChange = history[1] - event.values[1];

        history[0] = event.values[0];
        history[1] = event.values[1];

        if (xChange > threshold_x) {
            x = -1;
        } else if (xChange < -threshold_x) {
            x = 1;
        }

        if (yChange > threshold_y){
            y = -1;
        }else if (yChange < -threshold_y) {
            y = 1;
        }

        Show_Toast(++counter+" >> x : "+xChange+"   y : "+yChange);
//        gesture.recordMove(x, y);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.e("add", "accuracy_changed");
    }

    public void Show_Toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
