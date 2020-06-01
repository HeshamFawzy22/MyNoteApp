package com.example.mynoteapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mynoteapp.notesApp.HomeActivity;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                finish();
            }
        },2000);
    }

}
