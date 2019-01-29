package com.xencosworks.musicplayer;

import java.util.Comparator;

/**
 * Created by Bola on 1/29/2019.
 */

public class SortByTrackNumber implements Comparator <Song>{

    @Override
    public int compare(Song o1, Song o2) {
        return o1.getDetails()[3].compareTo(o2.getDetails()[3]);
    }
}
