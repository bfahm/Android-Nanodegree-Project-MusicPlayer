package com.xencosworks.musicplayer;

/**
 * Created by Bola on 1/27/2019.
 */

public class Album {
    private String mAlbumTitle;
    private String mAlbumTrackCount;
    private String mAlbumCoverArt;


    public Album(String albumTitle, String albumTrackCount, String albumCoverArt) {
        mAlbumTitle = albumTitle;
        mAlbumTrackCount = albumTrackCount;
        mAlbumCoverArt = albumCoverArt;
    }


    public String[] getDetails(){
        String details[] = new String[3];
        details[0] = mAlbumTitle;
        details[1] = mAlbumTrackCount;
        details[2] = mAlbumCoverArt;
        return details;
    }
}
