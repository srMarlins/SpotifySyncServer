package com.andrewkingmarshall.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jfowler on 3/24/16.
 */
public class TestSongListModel {
    private List<Track> trackList = new ArrayList<>();

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
    }

    public void addSong(Track track) {
        trackList.add(track);
    }
}
