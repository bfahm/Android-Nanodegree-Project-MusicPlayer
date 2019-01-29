package com.xencosworks.musicplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

public class Library extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 0;
    CardView errorMessage;
    private ArrayList<Song> songsList = new ArrayList<>();
    private ArrayList<Artist> artistList = new ArrayList<>();
    private ArrayList<Album> albumList = new ArrayList<>();
    private ListView listView;
    private LinearLayout expandSort;
    private int currentSetup = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        expandSort = findViewById(R.id.library_expandable_sort);

        CardView bottomWidget = findViewById(R.id.bottomCard);
        bottomWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Activity currentActivity = this;

        errorMessage = findViewById(R.id.error_message);
        listView = findViewById(R.id.list);
        Button grantAccess = findViewById(R.id.grant_access);
        grantAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(currentActivity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
            }
        });

        checkPermission();

        ImageView sortBy = findViewById(R.id.library_sort_by_btn);
        sortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandSort.getVisibility() == View.GONE) {
                    toggleSearchBar(0);
                } else {
                    toggleSearchBar(1);
                }


            }
        });

        RadioGroup radioSortBy = findViewById(R.id.radio_sort_by);
        final RadioButton rbSongs = findViewById(R.id.radio_songs);
        rbSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSearchBar(1);
                currentSetup = 1;
                getLocalSongs();
            }
        });
        final RadioButton rbArtist = findViewById(R.id.radio_artist);
        rbArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSearchBar(1);
                currentSetup = 2;
                getLocalArtists();
            }
        });
        final RadioButton rbAlbum = findViewById(R.id.radio_album);
        rbAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSearchBar(1);
                currentSetup = 3;
                getLocalAlbums();
            }
        });
        radioSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case 1:
                        rbSongs.setChecked(true);
                        rbArtist.setChecked(false);
                        rbAlbum.setChecked(false);
                        break;
                    case 2:
                        rbSongs.setChecked(false);
                        rbArtist.setChecked(true);
                        rbAlbum.setChecked(false);
                        break;
                    case 3:
                        rbSongs.setChecked(false);
                        rbArtist.setChecked(false);
                        rbAlbum.setChecked(true);
                        break;
                }
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            TextView bottomWidgetDetails = findViewById(R.id.bottomCard_currentActiveDetails);
            if (intent.getStringExtra("Title") != null) {
                bottomWidgetDetails.setText(intent.getStringExtra("Title") + " - " + intent.getStringExtra("Artist"));
            } else {
                bottomWidgetDetails.setText(R.string.lib_wid_select_a_track);
            }
        }

    }

    private void toggleSearchBar(int toggleCode) {
        LinearLayout currentSetup = findViewById(R.id.current_setup);
        ImageView playFab = findViewById(R.id.play_fab);
        View darkenView = findViewById(R.id.darken_screen);
        ImageView tappableIcon = findViewById(R.id.library_sort_by_btn);
        ImageView searchIcon = findViewById(R.id.search_bar_search_btn);
        TextView searchInfo = findViewById(R.id.search_info_text);

        switch (toggleCode) {
            case 0:
                expandSort.setVisibility(View.VISIBLE);
                currentSetup.setVisibility(View.GONE);
                playFab.setVisibility(View.GONE);
                darkenView.setVisibility(View.VISIBLE);
                tappableIcon.setImageResource(R.drawable.ic_ham_menu_dark);
                listView.setVisibility(View.GONE);
                searchIcon.setVisibility(View.GONE);
                searchInfo.setText(R.string.lib_search_sortby);
                break;
            case 1:
                expandSort.setVisibility(View.GONE);
                currentSetup.setVisibility(View.VISIBLE);
                playFab.setVisibility(View.VISIBLE);
                darkenView.setVisibility(View.GONE);
                tappableIcon.setImageResource(R.drawable.ic_ham_menu_sort);
                listView.setVisibility(View.VISIBLE);
                searchIcon.setVisibility(View.VISIBLE);
                searchInfo.setText(R.string.lib_search_search);
                break;
        }

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                errorMessage.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            errorMessage.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            getLocalSongs();

        }
    }

    private void getLocalSongs() {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TRACK
        };

        Cursor cursor = this.managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);

        songsList.clear();
        while (cursor.moveToNext()) {
            songsList.add(new Song(cursor.getString(0), cursor.getString(1), convertDuration(cursor.getString(2)), cursor.getString(3)));
        }

        Collections.sort(songsList, new SortByTitle());

        int resultCount = songsList.size();
        TextView trackCount = findViewById(R.id.track_count);
        trackCount.setText(resultCount + " Track");

        TextView stateSortedBy = findViewById(R.id.current_setup_sorted_by);
        stateSortedBy.setText("Songs");

        viewSongs();
    }

    private void getLocalArtists() {
        String[] projection = {
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
        };

        Cursor cursor = this.managedQuery(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);

        artistList.clear();
        while (cursor.moveToNext()) {
            artistList.add(new Artist(cursor.getString(0), cursor.getString(1)));
        }

        int resultCount = artistList.size();
        TextView trackCount = findViewById(R.id.track_count);
        trackCount.setText(resultCount + " Artist");

        TextView stateSortedBy = findViewById(R.id.current_setup_sorted_by);
        stateSortedBy.setText("Artists");

        viewArtists();
    }

    private void getLocalAlbums() {
        String[] projection = {
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                MediaStore.Audio.Albums.ALBUM_ART
        };

        Cursor cursor = this.managedQuery(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);

        albumList.clear();
        while (cursor.moveToNext()) {
            albumList.add(new Album(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
        }

        int resultCount = albumList.size();
        TextView trackCount = findViewById(R.id.track_count);
        trackCount.setText(resultCount + " Albums");

        TextView stateSortedBy = findViewById(R.id.current_setup_sorted_by);
        stateSortedBy.setText("Albums");

        viewAlbums();
    }

    private void viewSongs() {
        SongAdapter itemsAdapter = new SongAdapter(this, songsList);

        listView.setAdapter(itemsAdapter);

        final CardView bottomWidget = findViewById(R.id.bottomCard);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                {
                    if (firstVisibleItem + visibleItemCount == totalItemCount) {
                        bottomWidget.setVisibility(View.GONE);
                    } else {
                        bottomWidget.setVisibility(View.VISIBLE);
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

                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentSetup == 1) {
                    Intent intent = new Intent(view.getContext(), Details.class);
                    intent.putExtra("Title", songsList.get(position).getDetails()[0]);
                    intent.putExtra("Artist", songsList.get(position).getDetails()[1]);
                    intent.putExtra("Duration", songsList.get(position).getDetails()[2]);
                    intent.putExtra("stateCode", 0 + "");
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    private void viewArtists() {
        ArtistAdapter itemsAdapter = new ArtistAdapter(this, artistList);

        listView.setAdapter(itemsAdapter);

        final CardView bottomWidget = findViewById(R.id.bottomCard);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                {
                    if (firstVisibleItem + visibleItemCount == totalItemCount) {
                        bottomWidget.setVisibility(View.GONE);
                    } else {
                        bottomWidget.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), Details.class);
                intent.putExtra("Artist", artistList.get(position).getmArtistName());
                intent.putExtra("Track Count", artistList.get(position).getmTrackCount());
                intent.putExtra("stateCode", 1 + "");
                startActivity(intent);

            }
        });
    }

    private void viewAlbums() {
        AlbumAdapter itemsAdapter = new AlbumAdapter(this, albumList);

        listView.setAdapter(itemsAdapter);

        final CardView bottomWidget = findViewById(R.id.bottomCard);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                {
                    if (firstVisibleItem + visibleItemCount == totalItemCount) {
                        bottomWidget.setVisibility(View.GONE);
                    } else {
                        bottomWidget.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), Details.class);
                intent.putExtra("Album", albumList.get(position).getDetails()[0]);
                intent.putExtra("Track Count", albumList.get(position).getDetails()[1]);
                intent.putExtra("Album Art", albumList.get(position).getDetails()[2]);
                intent.putExtra("stateCode", 2 + "");
                startActivity(intent);

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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    errorMessage.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    getLocalSongs();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    errorMessage.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
