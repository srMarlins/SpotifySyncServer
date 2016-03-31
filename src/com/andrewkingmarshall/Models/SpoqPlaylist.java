package com.andrewkingmarshall.Models;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by jfowler on 3/31/16.
 */
public class SpoqPlaylist {
    private int syncSongIndex;
    private boolean currentlyPlaying;
    private long songSyncTimestamp;
    private ArrayList<SpoqTrack> trackList;

    public int getSyncSongIndex() {
        return syncSongIndex;
    }

    public void setSyncSongIndex(int syncSongIndex) {
        this.syncSongIndex = syncSongIndex;
    }

    public boolean isCurrentlyPlaying() {
        return currentlyPlaying;
    }

    public void setCurrentlyPlaying(boolean currentlyPlaying) {
        this.currentlyPlaying = currentlyPlaying;
    }

    public long getSongSyncTimestamp() {
        return songSyncTimestamp;
    }

    public void setSongSyncTimestamp(long songSyncTimestamp) {
        this.songSyncTimestamp = songSyncTimestamp;
    }

    public ArrayList<SpoqTrack> getTrackList() {
        return trackList;
    }

    public void setTrackList(ArrayList<SpoqTrack> trackList) {
        this.trackList = trackList;
    }

    public void addSpoqTrack(SpoqTrack track){
        trackList.add(track);
    }

    public void removeSpoqTrack(SpoqTrack track){
        Iterator<SpoqTrack> setIterator = trackList.iterator();
        while (setIterator.hasNext()) {
            SpoqTrack o = setIterator.next();
            if (track.getTrackId().equals(o.getTrackId())) {
                setIterator.remove();
            }
        }
    }
}
