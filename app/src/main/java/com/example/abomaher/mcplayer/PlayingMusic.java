package com.example.abomaher.mcplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class PlayingMusic extends AppCompatActivity {
    MediaPlayer mMediaPlayer;
    int fProgress = 0;
    SeekBar seekBar;
    String fullpath, title, artist, art;
    int position;
    TextView musicTitle, musicArtist;
    boolean play_pause;
    ImageView img;
    ImageButton rewindButton, forwardButton, playImg, playList;
    RelativeLayout playPauseButton;
    Bitmap bmp = null;
    Intent intent;

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mMediaPlayer.stop();
            finish();
        } catch (Exception e) {
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            mMediaPlayer.stop();
            finish();
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mMediaPlayer.stop();
            finish();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_playing);
        intent = new Intent(getApplicationContext(), MainActivity.class);
        play_pause = false;
        img = (ImageView) findViewById(R.id.thumbnail);
        musicTitle = (TextView) findViewById(R.id.music_title);
        musicArtist = (TextView) findViewById(R.id.music_Artist);
        myThread thread = new myThread();
        //for seekbar change
        seekBar = (SeekBar) findViewById(R.id.nowPlayingSeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()

        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(fProgress);
            }
        });
        //to get data from play music activity
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position");
        fullpath = bundle.getString("fullpath");
        title = bundle.getString("title");
        artist = bundle.getString("artist");
        art = bundle.getString("art");
        musicTitle.setText(title);
        musicArtist.setText(artist);
        if (art == "No Art") {
            img.setImageResource(R.drawable.clipart);
        } else {
            img.setImageBitmap(getMusicArt(art));
        }

        //playing music
        Uri uri = Uri.parse(fullpath);
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(this, uri);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
        seekBar.setMax(mMediaPlayer.getDuration());
        //song_name = (TextView) rootView.findViewById(R.id.song_name);
        //song_name.setText(arrayList.get(position).getSong_Name());
        thread.start();
        //decleare previousButton , playPauseButton and nextButton ImageButton
        rewindButton = (ImageButton) findViewById(R.id.rewindButton);
        playPauseButton = (RelativeLayout) findViewById(R.id.playPauseButtonBackground);
        playImg = (ImageButton) findViewById(R.id.playPauseButton);
        playList = (ImageButton) findViewById(R.id.playlist);
        forwardButton = (ImageButton) findViewById(R.id.forwardButton);
        playList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mMediaPlayer.stop();
                } catch (Exception e) {
                }
                startActivity(intent);
            }
        });
        //for play image button
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (play_pause == false) {
                    mMediaPlayer.pause();
                    play_pause = true;
                    playImg.setImageResource(R.drawable.btn_playback_play_light);
                } else {
                    mMediaPlayer.start();
                    play_pause = false;
                    playImg.setImageResource(R.drawable.btn_playback_pause_light);
                }
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int CurrentDuration = mMediaPlayer.getCurrentPosition();
                mMediaPlayer.seekTo((int) (CurrentDuration + 5000L));
            }
        });

        rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int CurrentDuration = mMediaPlayer.getCurrentPosition();
                mMediaPlayer.seekTo((int) (CurrentDuration - 5000L));
            }
        });

    }

    //Thread for seek bar to update progress
    class myThread extends Thread {
        @Override
        public void run() {
            while (mMediaPlayer != null) {
                try {
                    myThread.sleep(1000L);
                } catch (Exception e) {
                }
                seekBar.post(new Runnable() {
                    @Override
                    public void run() {
                        seekBar.setProgress(mMediaPlayer.getCurrentPosition());
                    }
                });
            }
        }
    }

    //for getting music art
    public Bitmap getMusicArt(String art) {
        try {
            FileInputStream is = this.openFileInput(art);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
