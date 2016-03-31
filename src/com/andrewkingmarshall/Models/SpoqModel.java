package com.andrewkingmarshall.Models;

import java.util.HashMap;

/**
 * Created by jfowler on 3/31/16.
 */
public class SpoqModel {
    private int playlistId;
    /**
     * userMap uses the user's music service id (Spotify for now) as the key to retrieve the SpoqUser info
     */
    private HashMap<String, SpoqUser> userMap;
    private SpoqPlaylist playlist;
}
