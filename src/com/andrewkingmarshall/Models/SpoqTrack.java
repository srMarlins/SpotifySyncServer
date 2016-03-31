package com.andrewkingmarshall.Models;

import java.util.Iterator;
import java.util.List;

/**
 * Created by jfowler on 3/31/16.
 */
public class SpoqTrack {
    private String trackId;
    private boolean skipped;
    private List<SpoqUser> usersThatVoted;

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public boolean isSkipped() {
        return skipped;
    }

    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
    }

    public List<SpoqUser> getUsersThatVoted() {
        return usersThatVoted;
    }

    public void setUsersThatVoted(List<SpoqUser> usersThatVoted) {
        this.usersThatVoted = usersThatVoted;
    }

    public void addDownVote(SpoqUser userThatVoted) {
        usersThatVoted.add(userThatVoted);
    }

    public void removeDownVote(SpoqUser userToRemove) {
        Iterator<SpoqUser> setIterator = usersThatVoted.iterator();
        while (setIterator.hasNext()) {
            SpoqUser o = setIterator.next();
            if (o.getMusicServiceId().equals(userToRemove.getMusicServiceId())) {
                setIterator.remove();
            }
        }
    }
}
