package com.andrewkingmarshall;

import com.andrewkingmarshall.Models.Track;
import com.andrewkingmarshall.Models.TrackQueue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/queue")
public class WebSocketEndPoint {

    public static final char DELIMITER = '!';
    public static final String ERROR_COMMAND = "error!";

    private TrackQueueHandler queueHandler = new TrackQueueHandler();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println("Connected: " + session.getId());
    }

    @OnMessage
    public void getMessage(final String message, final Session session) {
        /*if (!sessionQueueMap.contains(session)) {
            connectedSessions.add(session);
        }*/
        try {
            handleMessage(session, message);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @OnClose
    public void closeConnectionHandler(Session session, CloseReason closeReason) {
        queueHandler.removeUserFromQueue(session);
    }

    public String[] splitCommandFromJson(String incomingCommand) {
        String[] commandAndJson = new String[2];
        commandAndJson[0] = incomingCommand.substring(0, incomingCommand.indexOf(DELIMITER) + 1);
        commandAndJson[1] = incomingCommand.substring(incomingCommand.indexOf(DELIMITER), incomingCommand.length() + 1);
        return commandAndJson;
    }

    public void handleMessage(Session session, String message) throws IOException {
        String[] commandAndJson = splitCommandFromJson(message);
        String command = commandAndJson[0];
        String messageJson = commandAndJson[1];

        switch (command) {
            case TrackQueueHandler.TrackQueueCommands.START_QUEUE:
                startQueue(session, messageJson);
                break;
            case TrackQueueHandler.TrackQueueCommands.JOIN_QUEUE:
                break;
            case TrackQueueHandler.TrackQueueCommands.LEAVE_QUEUE:
                break;
            case TrackQueueHandler.TrackQueueCommands.ADD_SONG:
                addSong(session, messageJson);
                break;
            case TrackQueueHandler.TrackQueueCommands.DOWN_VOTE:
                break;
        }
    }

    private void startQueue(Session session, String json) throws IOException {
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        String userId = jsonObject.get("userId").getAsString();
        if (queueHandler.startQueue(session, userId)) {
            queueHandler.broadcastQueueUpdate(queueHandler.getConnectedQueue(session));
        } else {
            session.getBasicRemote().sendText(ERROR_COMMAND);
        }
    }

    private void joinQueue(Session session, String json) {
        //TODO-Parse queueId from json
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        long queueId = jsonObject.get("queueId").getAsLong();
        String userId = jsonObject.get("userId").getAsString();
        queueHandler.addUser(session, queueId, userId);
    }

    private void leaveQueue(Session session, String json) {

    }

    private void addSong(Session session, String json) throws IOException {
        Gson gson = new Gson();
        Track track = gson.fromJson(json, Track.class);
        if (queueHandler.sessionIsConnected(session)) {
            TrackQueue usersQueue = queueHandler.getConnectedQueue(session);
            if (usersQueue != null) {
                usersQueue.addTrackId(track.getId());
            } else {
                session.getBasicRemote().sendText(ERROR_COMMAND);
            }
            queueHandler.broadcastQueueUpdate(usersQueue);
        } else {
            session.getBasicRemote().sendText(ERROR_COMMAND);
        }
    }

    private void downVoteSong() {

    }

}