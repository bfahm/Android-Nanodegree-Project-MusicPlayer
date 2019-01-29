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

public class ArtistAdapter extends ArrayAdapter<Artist> {
    public ArtistAdapter(Activity context, ArrayList<Artist> artists) {
        super(context,0,  artists);
    }

    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Artist currentArtist = getItem(position);

        TextView artistTitle = listItemView.findViewById(R.id.title_main);
        artistTitle.setText(currentArtist.getmArtistName());

        TextView artistDetails = listItemView.findViewById(R.id.subtitle);
        artistDetails.setText(currentArtist.getmTrackCount()+" Tracks");

        ImageView albumCover = listItemView.findViewById(R.id.album_cover);
        albumCover.setVisibility(View.GONE);


        return listItemView;
    }
}
