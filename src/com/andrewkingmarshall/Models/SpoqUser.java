package com.andrewkingmarshall.Models;

/**
 * Created by jfowler on 3/31/16.
 */
public class SpoqUser {
    private String userName;
    private String sessionId;
    private boolean isAdmin;
    /**
     * This is the id of the user assigned by the music service being used (Spotify for now)
     */
    private String musicServiceId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getSpotifyId() {
        return musicServiceId;
    }

    public void setSpotifyId(String spotifyId) {
        this.musicServiceId = spotifyId;
    }


}
