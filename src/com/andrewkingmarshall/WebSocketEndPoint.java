package com.andrewkingmarshall;

import com.andrewkingmarshall.Models.TestSongListModel;
import com.andrewkingmarshall.Models.Track;
import com.google.gson.Gson;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;  
import java.util.LinkedList;  
import java.util.List;

@ServerEndpoint(value = "/queue")
public class WebSocketEndPoint {

    private static List<Session> connectedSessions = new LinkedList<>();
    private TestSongListModel testSongListModel = new TestSongListModel();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println("Connected: " + session.getId());
    }

    @OnMessage
    public void getMessage(final String message, final Session session) {
        if (!connectedSessions.contains(session)) {
            connectedSessions.add(session);
        }
        Gson gson = new Gson();
        Track track = gson.fromJson(message, Track.class);
        testSongListModel.addSong(track);
        String outgoingMessage = gson.toJson(testSongListModel);
        broadcastToAll(outgoingMessage);
    }

    private void broadcastToAll(final String s) {
        for (Session session : connectedSessions) {
            try {
                session.getBasicRemote().sendText(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void closeConnectionHandler(Session session, CloseReason closeReason) {
        connectedSessions.remove(session);
    }

}