package com.example.hackoverflow;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class qr extends AppCompatActivity {


    private IntentIntegrator qrScan;

    public String qr_result;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        qrScan = new IntentIntegrator(this);

        qrScan.setOrientationLocked(false);
        qrScan.setBeepEnabled(true);
        qrScan.setPrompt(" ");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        qrScan.initiateScan();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                } catch (JSONException e) {
                    e.printStackTrace();
                    qr_result = result.getContents();
                    perform();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void perform(){

        if(qr_result.equals("Block1")){

            Toast.makeText(this,"You're in Block1.",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(qr.this, block1.class);
            startActivity(intent);

        }
        else if(qr_result.equals("Block2")){

            Toast.makeText(this,"You're in Block2.",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(qr.this, block2.class);
            startActivity(intent);

        }
    }


}
