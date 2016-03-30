package com.andrewkingmarshall;

import com.andrewkingmarshall.Models.SimpleUser;
import com.andrewkingmarshall.Models.Track;
import com.andrewkingmarshall.Models.TrackQueue;
import com.google.gson.Gson;

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
        System.out.println("Message received");
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
        System.out.println("Connection closed: " + session.getId());
        queueHandler.removeUserFromQueue(session);
    }

    public String[] splitCommandFromJson(String incomingCommand) {
        String[] commandAndJson = new String[2];
        commandAndJson[0] = incomingCommand.substring(0, incomingCommand.indexOf(DELIMITER) + 1);
        commandAndJson[1] = incomingCommand.substring(incomingCommand.indexOf(DELIMITER) + 1, incomingCommand.length());
        return commandAndJson;
    }

    public void handleMessage(Session session, String message) throws IOException {
        String[] commandAndJson = splitCommandFromJson(message);
        String command = commandAndJson[0].toLowerCase();
        String messageJson = commandAndJson[1];

        System.out.print("Command: " + command + "\n");
        System.out.print("Json: " + messageJson + "\n");

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
        Gson gson = new Gson();
        SimpleUser user = gson.fromJson(json, SimpleUser.class);
        queueHandler.startQueue(session, user.getUserId());
        queueHandler.broadcastQueueUpdate(queueHandler.getConnectedQueue(session));
    }

    private void joinQueue(Session session, String json) {
        Gson gson = new Gson();
        SimpleUser user = gson.fromJson(json, SimpleUser.class);
        queueHandler.addUser(session, user.getQueueId(), user.getUserId());
    }

    private void leaveQueue(Session session, String json) {

    }

    private void addSong(Session session, String json) throws IOException {
        Gson gson = new Gson();
        Track track = gson.fromJson(json, Track.class);
        TrackQueue usersQueue = queueHandler.getConnectedQueue(session);

        if (usersQueue != null) {
            usersQueue.addTrackId(track.getId());
        } else {
            session.getBasicRemote().sendText(ERROR_COMMAND);
        }
        queueHandler.broadcastQueueUpdate(usersQueue);
    }

    private void downVoteSong() {

    }

}