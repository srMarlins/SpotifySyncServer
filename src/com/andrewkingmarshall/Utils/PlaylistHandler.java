package com.andrewkingmarshall.Utils;

import com.andrewkingmarshall.Exceptions.PlaylistNotFoundException;
import com.andrewkingmarshall.Models.SpoqModel;
import com.andrewkingmarshall.Models.SpoqTrack;
import com.andrewkingmarshall.Models.SpoqUser;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jfowler on 3/29/16.
 */
public class PlaylistHandler {

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    private static PlaylistHandler playlistHandler;
    //Map of queueId to it's matching Spoq object
    private HashMap<Integer, SpoqModel> playlistMap = new HashMap<>();

    public static PlaylistHandler getInstance(){
        if(playlistHandler == null){
            playlistHandler = new PlaylistHandler();
        }
        return playlistHandler;
    }

    public boolean playlistExists(int playlistID){
        return playlistMap.get(playlistID) != null;
    }

    public int createPlaylist(SpoqUser user){
        SpoqModel spoqModel = new SpoqModel();
        int playlistId = atomicInteger.incrementAndGet();
        user.setConnectedPlaylistId(playlistId);
        user.setAdmin(true);
        spoqModel.setPlaylistId(playlistId);
        spoqModel.addUser(user);
        playlistMap.put(playlistId, spoqModel);
        return playlistId;
    }

    public void joinPlaylist(SpoqUser spoqUser) throws PlaylistNotFoundException{
        if(playlistExists(spoqUser.getConnectedPlaylistId())) {
            spoqUser.setAdmin(false);
            playlistMap.get(spoqUser.getConnectedPlaylistId()).addUser(spoqUser);
        }
    }

    public void leavePlaylist(SpoqUser spoqUser) throws PlaylistNotFoundException{
        if(playlistExists(spoqUser.getConnectedPlaylistId())) {
            playlistMap.get(spoqUser.getConnectedPlaylistId()).removeUser(spoqUser.getMusicServiceId());
        }
    }

    public void removePlaylist(int playlistId){
        if(playlistMap.remove(playlistId) != null){
            atomicInteger.decrementAndGet();
        }
    }

    public void addTrack(int playlistId, SpoqTrack track) throws PlaylistNotFoundException{
        if(playlistExists(playlistId)) {
            playlistMap.get(playlistId).getPlaylist().addSpoqTrack(track);
        }else{
            throw new PlaylistNotFoundException();
        }
    }

    public void removeTrack(int playlistId, SpoqTrack track) throws PlaylistNotFoundException{
        if(playlistExists(playlistId)) {
            playlistMap.get(playlistId).getPlaylist().removeSpoqTrack(track);
        }else{
            throw new PlaylistNotFoundException();
        }
    }

    public void downVoteTrack(SpoqTrack track, SpoqUser user) throws PlaylistNotFoundException{
        if (playlistExists(user.getConnectedPlaylistId())){
            playlistMap.get(user.getConnectedPlaylistId()).getPlaylist().downVoteSpoqTrack(track, user);
        }else{
            throw new PlaylistNotFoundException();
        }
    }

    public void removeDownVote(SpoqTrack track, SpoqUser user) throws PlaylistNotFoundException{
        if(playlistExists(user.getConnectedPlaylistId())) {
            playlistMap.get(user.getConnectedPlaylistId()).getPlaylist().removeSpoqTrack(track);
        }else{
            throw new PlaylistNotFoundException();
        }
    }

    public SpoqModel getSpoqModel(int playlistId){
        return playlistMap.get(playlistId);
    }

    public static class TrackQueueCommands {
        public static final String CREATE_PLAYLIST = "create_playlist!";
        public static final String JOIN_PLAYLIST = "join_playlist!";
        public static final String LEAVE_PLAYLIST = "leave_playlist!";
        public static final String ADD_SONG = "add_song!";
        public static final String DOWN_VOTE = "down_vote!";
        public static final String REMOVE_DOWN_VOTE = "remove_down_vote!";
    }


}
