package com.xencosworks.musicplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String currentTitle = "";
        String currentArtist = "";

        TextView title = findViewById(R.id.main_song_title);
        TextView artist = findViewById(R.id.main_song_artist);
        TextView duration = findViewById(R.id.main_song_duration);

        Intent intent = getIntent();
        if (intent != null) {

            currentTitle = intent.getStringExtra("Title");
            title.setText(currentTitle);


            currentArtist = intent.getStringExtra("Artist");
            artist.setText(currentArtist);

            if (intent.getStringExtra("Duration") != null) {
                duration.setText(intent.getStringExtra("Duration"));
            }

        }

        final String finalCurrentTitle = currentTitle;
        final String finalCurrentArtist = currentArtist;
        ImageView btnLibrary = findViewById(R.id.btn_library);
        btnLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Library.class);

                if (finalCurrentTitle != null) {
                    intent.putExtra("Title", finalCurrentTitle);
                    intent.putExtra("Artist", finalCurrentArtist);
                }
                startActivity(intent);
            }
        });
    }
}
