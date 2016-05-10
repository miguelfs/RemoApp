package ufrj.remo.lib.com.remoapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private float mAccelerationX, mAccelerationY, mAccelerationZ;
    private String mAccelerationXString, mAccelerationYString, mAccelerationZString;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private static final float EPSILON = 0.000001f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;

    private TextView mSensorsTextView, mSensorsValueTextView, mGyroscopeValueTextView;
    private SensorManager mSensorManager;
    private SensorEventListener mSensorEventListener;
    private Sensor mAccelerometerSensor, mGyroscope, mLinearAccelerationSensor;
    private String mAvailableSensorsString;
    private  List<Sensor> deviceSensors;


    private final SensorEventListener mAccelerometerSensorListener = new SensorEventListener() {
    public void onSensorChanged(SensorEvent event) {
        mAccelerationX = event.values[0]; mAccelerationXString = Float.toString(mAccelerationX);
        mAccelerationY = event.values[1]; mAccelerationYString = Float.toString(mAccelerationY);
        mAccelerationZ = event.values[2]; mAccelerationZString = Float.toString(mAccelerationZ);
        if (mAccelerationXString.length() > 4)  mAccelerationXString = mAccelerationXString.substring(0, 4);
        if (mAccelerationYString.length() > 4)  mAccelerationYString = mAccelerationYString.substring(0, 4);
        if (mAccelerationZString.length() > 4)  mAccelerationZString = mAccelerationZString.substring(0, 4);
       // mAccelerationTextView.setText(" x = " +   mAccelerationXString + "\n y = " + mAccelerationYString + "\n z = " + mAccelerationZString);

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
};
/*
    private final SensorEventListener mGyroscopeSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            mGyroscopeValueTextView.setText(" x = " +   event.values[0] + "\n y = " + event.values[1] + "\n z = " + event.values[2]);
            // This timestep's delta rotation to be multiplied by the current rotation
            // after computing it from the gyro sample data.
            if (timestamp != 0) {
                final float dT = (event.timestamp - timestamp) * NS2S;
                // Axis of the rotation sample, not normalized yet.
                float axisX = event.values[0];
                float axisY = event.values[1];
                float axisZ = event.values[2];

                // Calculate the angular speed of the sample
                float omegaMagnitude = (float) Math.sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);

                // Normalize the rotation vector if it's big enough to get the axis
                if (omegaMagnitude > EPSILON) {
                    axisX /= omegaMagnitude;
                    axisY /= omegaMagnitude;
                    axisZ /= omegaMagnitude;
                }

                // Integrate around this axis with the angular speed by the timestep
                // in order to get a delta rotation from this sample over the timestep
                // We will convert this axis-angle representation of the delta rotation
                // into a quaternion before turning it into the rotation matrix.
                float thetaOverTwo = omegaMagnitude * dT / 2.0f;
                float sinThetaOverTwo = (float)Math.sin(thetaOverTwo);
                float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
                deltaRotationVector[0] = sinThetaOverTwo * axisX;
                deltaRotationVector[1] = sinThetaOverTwo * axisY;
                deltaRotationVector[2] = sinThetaOverTwo * axisZ;
                deltaRotationVector[3] = cosThetaOverTwo;
            }
            timestamp = event.timestamp;
            float[] deltaRotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
            // User code should concatenate the delta rotation we computed with the current rotation
            // in order to get the updated rotation.
            // rotationCurrent = rotationCurrent * deltaRotationMatrix;


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };*/

@Override
protected void onResume() {
    super.onResume();
    mSensorManager.registerListener(mAccelerometerSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
//    mSensorManager.registerListener(mGyroscopeSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_UI);
        }

@Override
protected void onPause() {
        mSensorManager.unregisterListener(mAccelerometerSensorListener);
//        mSensorManager.unregisterListener(mGyroscopeSensorListener);
        super.onPause();
        }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorsTextView = (TextView) findViewById(R.id.sensorsTextView);
        mGyroscopeValueTextView = (TextView) findViewById(R.id.valueGiroscopioSensorTextView);
        mSensorsValueTextView = (TextView) findViewById(R.id.valueSensorTextView);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

                deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for(int i = 0; i < deviceSensors.size(); i++) {
            mAvailableSensorsString = mAvailableSensorsString + deviceSensors.get(i).getName() + "\n";
        }
        mSensorsTextView.setText(mAvailableSensorsString);
        mSensorsTextView.setMovementMethod(new ScrollingMovementMethod());

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(mAccelerometerSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null){
            deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
            mSensorManager.registerListener(mAccelerometerSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_UI);
        }






    }




}
