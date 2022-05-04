package com.example.image_video_audio;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class Audio extends AppCompatActivity {
    MediaPlayer mediaPlayer = null;

    TextView audioNameTextView;
    TextView audioDurationTextView;
    TextView audioCurrentTextView;

    Button loadButton;
    Button playButton;
    Button pauseButton;
    Button audioCurrentButton;
    Button stopButton;

    ActivityResultLauncher<String> pickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null)
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), result);
                    if (mediaPlayer != null) {
                        audioNameTextView.setText("Load audio successful");
                        audioCurrentTextView.setText("00:00");
                        audioDurationTextView.setText(convertDurationToAudioTime(mediaPlayer.getDuration()));
                    } else {
                        Toast.makeText(Audio.this, "Failed to load audio", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        audioDurationTextView = findViewById(R.id.audio_duration_textview);
        audioNameTextView = findViewById(R.id.audio_name_textview);
        audioCurrentTextView = findViewById(R.id.audio_current_textview);
        loadButton = findViewById(R.id.load_button);
        loadButton.setOnClickListener(view -> pickerLauncher.launch("audio/*"));

        playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(view -> {
            if (mediaPlayer != null) {
                mediaPlayer.start();
                audioDurationTextView.setText(
                        convertDurationToAudioTime(mediaPlayer.getDuration()));
            }
        });

        pauseButton = findViewById(R.id.pause_button);
        pauseButton.setOnClickListener(view -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying())
                mediaPlayer.pause();
        });

        audioCurrentButton = findViewById(R.id.audio_current_button);
        audioCurrentButton.setOnClickListener(view -> {
            if (mediaPlayer != null)
                audioCurrentTextView.setText(
                        convertDurationToAudioTime(mediaPlayer.getCurrentPosition()));
        });

        stopButton = findViewById(R.id.stop_button);
        stopButton.setOnClickListener(view -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                audioNameTextView.setText("No name");
                audioCurrentTextView.setText("00:00");
                audioDurationTextView.setText("00:00");
            }
        });
    }

    private String convertDurationToAudioTime(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
    }

}