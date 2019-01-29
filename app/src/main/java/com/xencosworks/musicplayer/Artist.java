package com.xencosworks.musicplayer;


import java.util.ArrayList;

/**
 * Created by Bola on 1/27/2019.
 */

public class Artist {
    private String mArtistName;
    private int mTrackCount;


    public Artist(String artistName, String trackCount) {
        mArtistName = artistName;
        mTrackCount = Integer.parseInt(trackCount);
    }

    public String getmArtistName(){
        return mArtistName;
    }

    public String getmTrackCount(){
        return String.valueOf(mTrackCount);
    }
}
