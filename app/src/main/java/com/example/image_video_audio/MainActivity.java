package com.example.image_video_audio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermissions();
        Button audio = findViewById(R.id.audio);
        Button image = findViewById(R.id.image);
        Button video = findViewById(R.id.video);
        audio.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,Audio.class)));
        image.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,Image.class)));
        video.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,Video.class)));
    }

    private void checkAndRequestPermissions() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                + (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)))
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}