package com.xencosworks.musicplayer;

import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

public class DetailsActivity extends AppCompatActivity {

    private ListView trackList;
    private LinearLayout stateSongs;
    private LinearLayout stateArtist;
    private ArrayList<Song> songsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        stateSongs = findViewById(R.id.details_state_songs);
        trackList = findViewById(R.id.list_details);
        stateArtist = findViewById(R.id.details_state_artist);

        Intent intent = getIntent();
        int code = 0;
        try {
            code = Integer.parseInt(intent.getStringExtra("stateCode"));
        } catch (NumberFormatException e) {
            Log.v("DetailsActivity", "DetailsActivity called itself succefully");
        }
        switch (code) {
            case 0:
                stateSongs.setVisibility(View.VISIBLE);
                stateArtist.setVisibility(View.GONE);
                trackList.setVisibility(View.GONE);

                TextView title = findViewById(R.id.details_title);
                title.setText(intent.getStringExtra("Title"));

                TextView artist = findViewById(R.id.details_artist);
                artist.setText(intent.getStringExtra("Artist"));

                TextView duration = findViewById(R.id.details_duration);
                duration.setText(intent.getStringExtra("Duration"));
                break;
            case 1:
                displayTrackList(intent.getStringExtra("Artist"), null);
                break;
            case 2:
                displayTrackList(null, intent.getStringExtra("Album"));
        }

        View navBack = findViewById(R.id.details_navigate_back);
        navBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getLocalSongs(String customArtist, String customAlbum) {
        String selection = null;
        if (customArtist != null || customAlbum != null) {
            if (customArtist != null) {
                selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 AND " + MediaStore.Audio.Media.ARTIST + "==" + '"' + customArtist + '"';
            }
            if (customAlbum != null) {
                selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 AND " + MediaStore.Audio.Media.ALBUM + "==" + '"' + customAlbum + '"';
            }
        }
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TRACK,
                MediaStore.Audio.Media.DATA
        };

        Cursor cursor = this.managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);

        songsList.clear();
        while (cursor.moveToNext()) {
            songsList.add(new Song(cursor.getString(0), cursor.getString(1),
                    convertDuration(cursor.getString(2)), cursor.getString(3), cursor.getString(4)));
        }

        if (customArtist != null) {
            Collections.sort(songsList, new SortByTitle());
        } else if (customAlbum != null) {
            Collections.sort(songsList, new SortByTrackNumber());
        }

        TextView AlbumOrArtistSubTitle = findViewById(R.id.album_artist_subtitle);
        AlbumOrArtistSubTitle.setText(songsList.size() + " Tracks");
    }

    private void displayTrackList(String customArtist, String customAlbum) {
        if (customArtist != null) {
            getLocalSongs(customArtist, null);
            TextView AlbumOrArtistTitle = findViewById(R.id.album_artist_title);
            AlbumOrArtistTitle.setText(customArtist);
        } else if (customAlbum != null) {
            getLocalSongs(null, customAlbum);
            TextView AlbumOrArtistTitle = findViewById(R.id.album_artist_title);
            AlbumOrArtistTitle.setText(customAlbum);
        }

        trackList.setVisibility(View.VISIBLE);
        stateSongs.setVisibility(View.GONE);
        stateArtist.setVisibility(View.VISIBLE);

        SongAdapter itemsAdapter = new SongAdapter(this, songsList);

        ListView listView = findViewById(R.id.list_details);

        listView.setAdapter(itemsAdapter);

        final ImageView bottomFab = findViewById(R.id.detalis_fab);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                {
                    if (firstVisibleItem + visibleItemCount == totalItemCount) {
                        bottomFab.setVisibility(View.GONE);
                    } else {
                        bottomFab.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("Title", songsList.get(position).getDetails()[0]);
                intent.putExtra("Artist", songsList.get(position).getDetails()[1]);
                intent.putExtra("Duration", songsList.get(position).getDetails()[2]);
                intent.putExtra("Uri", songsList.get(position).getDataUri());
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                intent.putExtra("Title", songsList.get(position).getDetails()[0]);
                intent.putExtra("Artist", songsList.get(position).getDetails()[1]);
                intent.putExtra("Duration", songsList.get(position).getDetails()[2]);
                intent.putExtra("stateCode", 0);
                startActivity(intent);
                return true;
            }
        });
    }

    private String convertDuration(String originalDuration) {
        int dur = Integer.parseInt(originalDuration);

        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000;

        NumberFormat formatter = new DecimalFormat("00");
        String seconds = formatter.format(scs);

        String songTime = String.format("%02d:%02d", hrs, mns);
        return songTime + ":" + seconds.substring(0, 2);
    }
}
