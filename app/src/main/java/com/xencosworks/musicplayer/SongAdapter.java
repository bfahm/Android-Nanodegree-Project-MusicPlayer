package com.xencosworks.musicplayer;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bola on 1/28/2019.
 */

public class SongAdapter extends ArrayAdapter<Song> {
    public SongAdapter(Activity context, ArrayList<Song> songs) {
        super(context,0,  songs);
    }

    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Song currentSong = getItem(position);

        TextView songTitle = listItemView.findViewById(R.id.title_main);
        songTitle.setText(currentSong.getDetails()[0]);

        TextView artistDetails = listItemView.findViewById(R.id.subtitle);
        artistDetails.setText(currentSong.getDetails()[1]);

        ImageView albumCover = listItemView.findViewById(R.id.album_cover);
        albumCover.setImageResource(R.drawable.dummy_cover);


        return listItemView;
    }
}
