package com.example.hackoverflow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static java.lang.StrictMath.abs;

public class rssi extends AppCompatActivity implements SensorEventListener {



    private SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    float[] mGravity;
    float[] mGeomagnetic;

    public float rssiValue;
    public float rssiDistance;
    public float dirAngle;

    public Pair< Float, Float > ans;

    public float currentDirAngle=0;
    public float associatedDistance;

    ArrayList<Pair<Float, Float>> pairOfRssiAndDirection;

    Button scan, complete_scan;
    TextView rssi, distance, direction;
    ImageView dial, hands;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssi);

        scan = findViewById(R.id.scan);
        complete_scan = findViewById(R.id.complete_scan);
        rssi = findViewById(R.id.rssi);
        distance = findViewById(R.id.distance);
        direction = findViewById(R.id.direction);
        dial = findViewById(R.id.dial);
        hands = findViewById(R.id.hands);


        pairOfRssiAndDirection = new ArrayList<>();

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        scanning();
    }


    public void scanning(){

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkWifi();
                setValues();
                pairOfRssiAndDirection.add( new Pair<Float, Float>(rssiValue,dirAngle) );
                scanning();
            }
        });

        complete_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMaximum(pairOfRssiAndDirection);
            }
        });

    }


    public void getMaximum(ArrayList < Pair <Float,Float> > pairOfRssiAndDirection)
    {
        float max = Float.MAX_VALUE;
        ans = new Pair< Float, Float>(0.0f, 0.0f);

        for(int i=0; i<pairOfRssiAndDirection.size(); i++){
            float temp = abs(pairOfRssiAndDirection.get(i).first);                           //rssi
            if(temp<max){
                max = temp;
                ans = pairOfRssiAndDirection.get(i);
            }
//            if(temp==max){
//                if(dis<ans.second){
//                    ans = pairOfRssiAndDirection.get(i);
//                }
//            }
        }

        pairOfRssiAndDirection.removeAll(pairOfRssiAndDirection);

        associatedDistance = GetDistanceFromRssiAndTxPowerOn1m(ans.first,-45);

        rssi.setText(String.valueOf(ans.first));
        distance.setText(String.valueOf(associatedDistance));
        direction.setText(String.valueOf(ans.second));

        adjustArrow( ans.second );

        scanning();
    }


    private void adjustArrow( float b ) {
        Animation an = new RotateAnimation(0.0f, abs((float)(b-dirAngle)),Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        currentDirAngle = ans.second;

        an.setRepeatCount(0);
        an.setDuration(1000);
        an.setFillAfter(true);

        hands.startAnimation(an);
    }




    public void setValues(){
        rssi.setText("Rssi Value: "+ String.valueOf(rssiValue));
        distance.setText("Approximate Distance: "+ String.valueOf(rssiDistance));
        direction.setText("Direction: "+ String.valueOf(dirAngle));
    }



    public void checkWifi(){
        WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(wifiMgr.isWifiEnabled()){
            WifiInfo Info = wifiMgr.getConnectionInfo();
            if( Info.getNetworkId() == -1 ){
                Toast.makeText(this, "Not Connected", Toast.LENGTH_LONG).show();
            }
            else{
                calculateRssiAndDistance();
            }
        }
        else{
            Toast.makeText(this,"Turn On Your WiFi!",Toast.LENGTH_LONG).show();
        }
    }

    public void calculateRssiAndDistance(){
        WifiManager wifiCont = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        rssiValue = wifiCont.getConnectionInfo().getRssi();
        rssiDistance= (int) GetDistanceFromRssiAndTxPowerOn1m(rssiValue,-45);
    }

    public float GetDistanceFromRssiAndTxPowerOn1m(float rssiValue, int txPower){
        return (float)Math.pow(10, (txPower - rssiValue) / (10 * 2));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                float azimut = orientation[0]; // orientation contains: azimut, pitch and roll
                dirAngle = (int)Math.toDegrees(azimut);
                if (dirAngle < 0.0f) {
                    dirAngle += 360.0f;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
