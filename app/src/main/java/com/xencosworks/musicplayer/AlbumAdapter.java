package com.xencosworks.musicplayer;

import android.app.Activity;
import android.graphics.drawable.Drawable;
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

public class AlbumAdapter extends ArrayAdapter<Album> {
    public AlbumAdapter(Activity context, ArrayList<Album> albums) {
        super(context,0,  albums);
    }

    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Album currentAlbum = getItem(position);

        TextView songTitle = listItemView.findViewById(R.id.title_main);
        songTitle.setText(currentAlbum.getDetails()[0]);

        TextView artistDetails = listItemView.findViewById(R.id.subtitle);
        artistDetails.setText(currentAlbum.getDetails()[1]+" Tracks");

        ImageView albumCover = listItemView.findViewById(R.id.album_cover);
        Drawable img = Drawable.createFromPath(currentAlbum.getDetails()[2]);
        albumCover.setImageDrawable(img);


        return listItemView;
    }
}
