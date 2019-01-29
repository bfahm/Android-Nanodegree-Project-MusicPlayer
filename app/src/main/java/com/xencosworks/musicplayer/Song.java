package com.xencosworks.musicplayer;

/**
 * Created by Bola on 1/27/2019.
 */

public class Song {
    private String mSongTitle;
    private String mArtistDetails;
    private String mDuration;
    private String mId;


    public Song(String songTitle, String artistDetails, String duration, String id) {
        mSongTitle = songTitle;
        mArtistDetails = artistDetails;
        mDuration = duration;
        mId = id;
    }


    public String[] getDetails(){
        String details[] = new String[4];
        details[0] = mSongTitle;
        details[1] = mArtistDetails;
        details[2] = mDuration;
        details[3] = mId;
        return details;
    }
}
