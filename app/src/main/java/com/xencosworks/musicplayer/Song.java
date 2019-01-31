package com.xencosworks.musicplayer;

import android.net.Uri;

/**
 * Created by Bola on 1/27/2019.
 */

public class Song {
    private String mSongTitle;
    private String mArtistDetails;
    private String mDuration;
    private String mId;
    private String mData;


    public Song(String songTitle, String artistDetails, String duration, String id, String data) {
        mSongTitle = songTitle;
        mArtistDetails = artistDetails;
        mDuration = duration;
        mId = id;
        mData = data;
    }


    public String[] getDetails(){
        String details[] = new String[4];
        details[0] = mSongTitle;
        details[1] = mArtistDetails;
        details[2] = mDuration;
        details[3] = mId;
        return details;
    }

    public String getDataUri(){
        return mData;
    }
}
