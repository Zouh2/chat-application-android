package com.example.chatme.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatme.Constants.AllConstants;
import com.example.chatme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        firebaseAuth = FirebaseAuth.getInstance();

        mediaPlayer = MediaPlayer.create(this, R.raw.audio);
        mediaPlayer.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(SplashActivity.this, DashBoard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        }, 8000);


    }
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
        finish();
    }
}
