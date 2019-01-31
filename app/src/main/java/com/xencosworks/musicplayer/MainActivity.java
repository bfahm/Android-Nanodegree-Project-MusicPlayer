package com.xencosworks.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private String currentTitle = "";
    private String currentArtist = "";
    private String currentSongData = null;
    private int currentDuration = 0;
    private String newSongData = null;
    private int playerState = 0;
    private TextView title;
    private TextView artist;
    private TextView duration;
    private ImageButton playPause;
    private SeekBar seekBar;

    private MediaPlayer mediaPlayer = null;
    private AudioManager audioManager;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.main_song_title);
        artist = findViewById(R.id.main_song_artist);
        duration = findViewById(R.id.main_song_duration);
        playPause = findViewById(R.id.main_btn_play_pause);

        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        seekBar = findViewById(R.id.main_seekbar);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // useful if launch mode was set to singleTask, this will always get the 'latest' intent
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent != null) {

            currentTitle = intent.getStringExtra("Title");
            title.setText(currentTitle);

            currentArtist = intent.getStringExtra("Artist");
            artist.setText(currentArtist);

            currentDuration = convertToSeconds(intent.getStringExtra("Duration"));
            if (intent.getStringExtra("Duration") != null) {
                duration.setText(intent.getStringExtra("Duration"));
            }

            String trackUri = intent.getStringExtra("Uri");
            if(trackUri!=null){
                newSongData = trackUri;
                if (currentSongData!=newSongData){
                    currentSongData = newSongData;
                    handlePlayerTrackSwitch();
                    Log.v("***********************", "SHOULD ENTER SWITCHING ROUTE");
                }
            }
        }

        // populate Library Widget with data
        final String finalCurrentTitle = currentTitle;
        final String finalCurrentArtist = currentArtist;
        ImageButton btnLibrary = findViewById(R.id.btn_library);
        btnLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LibraryActivity.class);

                if (finalCurrentTitle != null) {
                    intent.putExtra("Title", finalCurrentTitle);
                    intent.putExtra("Artist", finalCurrentArtist);
                }
                startActivity(intent);
            }
        });

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePlayer(0);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null){
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange==AudioManager.AUDIOFOCUS_GAIN
                    ||focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT
                    ||focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                handlePlayer(0);
            }else if(focusChange==AudioManager.AUDIOFOCUS_LOSS){
                handlePlayer(1);
            }
        }
    };

    private int convertToSeconds(String s){
        String[] tokens = s.split(":");
        int hours = Integer.parseInt(tokens[0]);
        int minutes = Integer.parseInt(tokens[1]);
        int seconds = Integer.parseInt(tokens[2]);
        return 3600 * hours + 60 * minutes + seconds;
    }

    private void handlePlayer(int state){
        //state 0  -> playPause
        //state 1  -> next
        //state -1 -> previous
        switch (state){
            case 0:
                if (playerState==0){
                    try {
                        if(currentSongData!=null&&audioFocusManager()){
                            mediaPlayer = new MediaPlayer();
                            mediaPlayer.setDataSource(currentSongData);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            playPause.setImageResource(R.mipmap.ic_music_pause);
                            playerState=1;
                            seekBar.setMax(currentDuration);
                            //updateSeekbarAndDuration();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(playerState==1){
                    playPause.setImageResource(R.mipmap.ic_music_play);
                    mediaPlayer.pause();
                    playerState = 2;
                }else if(playerState == 2&&audioFocusManager()){
                    playPause.setImageResource(R.mipmap.ic_music_pause);
                    mediaPlayer.start();
                    playerState = 1;
                    //updateSeekbarAndDuration();
                }
                break;
            case 1:
                if(mediaPlayer!=null){
                    resetMediaPlayer();
                }
                break;
        }

    }

    private void handlePlayerTrackSwitch(){
        Log.v("***********************", ""+mediaPlayer);
        if(mediaPlayer!=null){
            mediaPlayer.release();
        }
            playerState = 0;
            handlePlayer(0);
            Log.v("***********************", "HAVE ENTERED SWITCHING ROUTE");

    }

    private void resetMediaPlayer(){
        mediaPlayer.stop();
        mediaPlayer.release();
        playerState =0;
        audioManager.abandonAudioFocus(afChangeListener);
        playPause.setImageResource(R.mipmap.ic_music_play);
    }

    private boolean audioFocusManager(){
        int result = audioManager.requestAudioFocus(afChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    private void updateSeekbarAndDuration(){
        seekBar.setMax(mediaPlayer.getDuration());

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        seekBar.setProgress(mediaPlayer.getCurrentPosition()/1000);
                    }
                    handler.postDelayed(this, 1000);
                }
            });

    }


}
