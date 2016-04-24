package com.andrewkingmarshall;

import com.andrewkingmarshall.Exceptions.PlaylistNotFoundException;
import com.andrewkingmarshall.Models.SpoqTrack;
import com.andrewkingmarshall.Models.SpoqUser;
import com.andrewkingmarshall.Utils.PlaylistHandler;
import com.andrewkingmarshall.Utils.SessionHandler;
import com.google.gson.Gson;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/queue")
public class WebSocketEndPoint {

    public static final char DELIMITER = '!';
    public static final String ERROR_COMMAND = "error!";

    private PlaylistHandler playlistHandler = PlaylistHandler.getInstance();
    private SessionHandler sessionHandler = SessionHandler.getInstance();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println("Connected: " + session.getId());
    }

    @OnMessage
    public void getMessage(final String message, final Session session) {
        System.out.println("Message received");
        try {
            if(handleMessage(session, message)){
                System.out.println("Message Parsed");
                int playlistId = sessionHandler.getUserFromSessionId(session.getId()).getConnectedPlaylistId();
                sessionHandler.broadcastSpoqUpdate(playlistHandler.getSpoqModel(playlistId));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void closeConnectionHandler(Session session, CloseReason closeReason) {
        System.out.println("Connection closed: " + session.getId());
        try {
            leavePlaylist(session);
        } catch (PlaylistNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String[] splitCommandFromJson(String incomingCommand) {
        String[] commandAndJson = new String[2];
        commandAndJson[0] = incomingCommand.substring(0, incomingCommand.indexOf(DELIMITER) + 1);
        commandAndJson[1] = incomingCommand.substring(incomingCommand.indexOf(DELIMITER) + 1, incomingCommand.length());
        return commandAndJson;
    }

    public boolean handleMessage(Session session, String message) throws IOException {
        System.out.println("parsing message: " + session.getId());
        boolean successfulMessage = false;
        String[] commandAndJson = splitCommandFromJson(message);
        String command = commandAndJson[0].toLowerCase();
        String messageJson = commandAndJson[1];

        System.out.print("Command: " + command + "\n");
        System.out.print("Json: " + messageJson + "\n");

        //TODO - Add error handling
        try {
            switch (command) {
                case PlaylistHandler.TrackQueueCommands.CREATE_PLAYLIST:
                    createPlaylist(session, messageJson);
                    break;
                case PlaylistHandler.TrackQueueCommands.JOIN_PLAYLIST:
                    joinPlaylist(session, messageJson);
                    break;
                case PlaylistHandler.TrackQueueCommands.LEAVE_PLAYLIST:
                    leavePlaylist(session);
                    break;
                case PlaylistHandler.TrackQueueCommands.ADD_SONG:
                    addSong(session, messageJson);
                    break;
                case PlaylistHandler.TrackQueueCommands.DOWN_VOTE:
                    downVoteSong(session, messageJson);
                case PlaylistHandler.TrackQueueCommands.REMOVE_DOWN_VOTE:
                    removeDownVote(session, messageJson);
                    break;
            }

            successfulMessage = true;
        } catch (PlaylistNotFoundException e){
            successfulMessage = false;
            System.out.println("PlaylistNotFoundException");
        }

        return successfulMessage;
    }

    private void createPlaylist(Session session, String json) {
        System.out.println("Creating playlist: " + session.getId());
        Gson gson = new Gson();
        SpoqUser user = gson.fromJson(json, SpoqUser.class);
        user.setSessionId(session.getId());
        playlistHandler.createPlaylist(user);
        sessionHandler.addSession(session, user);
    }

    private void joinPlaylist(Session session, String json) throws PlaylistNotFoundException{
        Gson gson = new Gson();
        SpoqUser user = gson.fromJson(json, SpoqUser.class);
        user.setSessionId(session.getId());
        playlistHandler.joinPlaylist(user);
        sessionHandler.addSession(session, user);
    }

    private void leavePlaylist(Session session) throws PlaylistNotFoundException{
        SpoqUser user = sessionHandler.getUserFromSessionId(session.getId());
        if (user == null){
            return;
        }
        playlistHandler.leavePlaylist(user);
        playlistHandler.removePlaylist(user.getConnectedPlaylistId());
        sessionHandler.removeSession(session.getId());
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addSong(Session session, String json) throws PlaylistNotFoundException{
        Gson gson = new Gson();
        SpoqTrack track = gson.fromJson(json, SpoqTrack.class);
        SpoqUser user = sessionHandler.getUserFromSessionId(session.getId());
        track.setTimeAdded(System.currentTimeMillis());
        playlistHandler.addTrack(user.getConnectedPlaylistId(), track);
    }

    private void downVoteSong(Session session, String json) throws PlaylistNotFoundException{
        Gson gson = new Gson();
        SpoqTrack track = gson.fromJson(json, SpoqTrack.class);
        SpoqUser user = sessionHandler.getUserFromSessionId(session.getId());
        playlistHandler.downVoteTrack(track, user);
    }

    private void removeDownVote(Session session, String json) throws PlaylistNotFoundException{
        Gson gson = new Gson();
        SpoqTrack track = gson.fromJson(json, SpoqTrack.class);
        SpoqUser user = sessionHandler.getUserFromSessionId(session.getId());
        playlistHandler.removeDownVote(track, user);
    }

}