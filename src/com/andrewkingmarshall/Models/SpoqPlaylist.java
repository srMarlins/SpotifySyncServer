package com.andrewkingmarshall.Models;

import java.util.LinkedHashMap;

/**
 * Created by jfowler on 3/31/16.
 */
public class SpoqPlaylist {
    private int syncSongIndex;
    private boolean currentlyPlaying;
    private long songSyncTimestamp;
    /**
     * Key - The track's id  Value - The SpoqTrack object
     */
    private LinkedHashMap<String, SpoqTrack> trackList;

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

    public LinkedHashMap<String, SpoqTrack> getTrackList() {
        return trackList;
    }

    public void setTrackList(LinkedHashMap<String, SpoqTrack> trackList) {
        this.trackList = trackList;
    }

    public void addSpoqTrack(SpoqTrack track) {
        trackList.put(track.getTrackId(), track);
    }

    public void removeSpoqTrack(SpoqTrack track) {
        trackList.remove(track.getTrackId());
    }

    public boolean downVoteSpoqTrack(SpoqTrack track, SpoqUser user) {
        SpoqTrack searchedTrack = trackList.get(track.getTrackId());
        if (searchedTrack != null) {
            searchedTrack.addDownVote(user);
        }
        return searchedTrack != null;
    }

    public boolean removeDownVote(SpoqTrack track, SpoqUser user) {
        SpoqTrack searchedTrack = trackList.get(track.getTrackId());
        if (searchedTrack != null) {
            searchedTrack.removeDownVote(user);
        }
        return searchedTrack != null;
    }
}
