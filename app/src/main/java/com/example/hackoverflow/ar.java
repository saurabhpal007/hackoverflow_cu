package com.example.hackoverflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ar extends AppCompatActivity {


    Button next_rssi, next_ar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);


        next_rssi = findViewById(R.id.next_rssi);
        next_ar = findViewById(R.id.next_ar);

        next_rssi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ar.this, rssi.class);
                startActivity(intent);
            }
        });


        next_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ar.this, ar_main.class);
                startActivity(intent);
            }
        });


    }
}
