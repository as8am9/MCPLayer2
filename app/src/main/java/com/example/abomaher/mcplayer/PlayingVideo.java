package com.example.abomaher.mcplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import java.util.ArrayList;

public class PlayingVideo extends AppCompatActivity {
    VideoView videoView;
    //this method to stop mediaplayer when activity stop
    @Override
    public void onStop() {
        super.onStop();
        try{ videoView.stopPlayback();}catch (Exception e ){}
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{ videoView.stopPlayback();}catch (Exception e ){}
    }

    //this method to stop mediaplayer when activity destroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        try{videoView.stopPlayback();}catch (Exception e){}
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_playing);
        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("position");
        String fullpath =  bundle.getString("fullpath");
        videoView = (VideoView) findViewById(R.id.videview);
        videoView.setVideoPath(fullpath);
        videoView.start();
    }
}
