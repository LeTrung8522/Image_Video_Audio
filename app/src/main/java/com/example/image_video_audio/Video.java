package com.example.image_video_audio;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Video extends AppCompatActivity {
    private static final String KEY_CURRENT_POSITION = "current_position";
    private static final String KEY_CURRENT_URI = "current_uri";

    private VideoView mVideoView;
    private Uri uriVideo;
    private int position = -1;

    ActivityResultLauncher<String> pickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        uriVideo = result;
                        mVideoView.setVideoURI(uriVideo);
                        mVideoView.start();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Button btn_addGallery = findViewById(R.id.btn_addGallery);
        // Find your VideoView in your video main xml layout
        mVideoView = findViewById(R.id.videoView_main);

        // create an object of media controller class
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);
        // set the media controller for video view
        mVideoView.setMediaController(mediaController);

        // implement on completion listener on video view
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                makeToast("Thank You...!!!");
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                makeToast("Oops An Error Occur While Playing Video...!!!");
                return false;
            }
        });

        btn_addGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickerLauncher.launch("video/*");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (uriVideo == null)
            return;
        position = mVideoView.getCurrentPosition();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (uriVideo == null)
            return;
        outState.putInt(KEY_CURRENT_POSITION, position);
        outState.putString(KEY_CURRENT_URI, uriVideo.toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int pos = savedInstanceState.getInt(KEY_CURRENT_POSITION);
        String path = savedInstanceState.getString(KEY_CURRENT_URI);
        uriVideo = Uri.parse(path);

        mVideoView.setVideoURI(uriVideo);
        mVideoView.seekTo(pos);
        mVideoView.start();
    }

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}