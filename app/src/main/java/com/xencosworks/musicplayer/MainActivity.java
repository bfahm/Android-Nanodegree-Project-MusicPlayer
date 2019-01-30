package com.xencosworks.musicplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String currentTitle = "";
    private String currentArtist = "";
    private TextView title;
    private TextView artist;
    private TextView duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        title = findViewById(R.id.main_song_title);
        artist = findViewById(R.id.main_song_artist);
        duration = findViewById(R.id.main_song_duration);
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
            Log.v("----------", "" + currentTitle);


            currentArtist = intent.getStringExtra("Artist");
            artist.setText(currentArtist);

            if (intent.getStringExtra("Duration") != null) {
                duration.setText(intent.getStringExtra("Duration"));
            }
        }

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
    }


}
