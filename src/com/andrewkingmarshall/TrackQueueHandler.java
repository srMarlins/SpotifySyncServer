package com.andrewkingmarshall;

import com.andrewkingmarshall.Models.TrackQueue;
import com.google.gson.Gson;

import javax.websocket.Session;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;

/**
 * Created by jfowler on 3/29/16.
 */
public class TrackQueueHandler {

    private long numQueues = 0L;
    //Map of queueId to it's matching queue object
    private HashMap<Long, TrackQueue> trackQueues;
    //Map of user sessions to it's connected queueId
    private HashMap<String, AbstractMap.SimpleEntry<Long, String>> sessionQueueIdMap;

    public TrackQueueHandler() {
        trackQueues = new HashMap<>();
        sessionQueueIdMap = new HashMap<>();
    }

    public void broadcastQueueUpdate(TrackQueue trackQueue) {
        for (Session session : trackQueue.getAllConnectedUsers().values()) {
            try {
                Gson gson = new Gson();
                String jsonMessage = gson.toJson(trackQueue);
                session.getBasicRemote().sendText(jsonMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean sessionIsConnected(Session session) {
        return sessionQueueIdMap.get(session.getId()) != null;
    }

    public TrackQueue getConnectedQueue(Session session) {
        long queueId = getConnectedQueueId(session);
        return trackQueues.get(queueId);
    }

    public boolean queueExists(long queueId) {
        return trackQueues.get(queueId) != null;
    }

    public long getConnectedQueueId(Session session) {
        return sessionQueueIdMap.get(session.getId()).getKey();
    }

    public void removeUserFromQueue(Session session) {
        String userId = sessionQueueIdMap.get(session.getId()).getValue();
        long queueId = getConnectedQueueId(session);
        trackQueues.get(queueId).removeGenericUser(userId);
        sessionQueueIdMap.remove(session.getId());
    }

    public void addUser(Session session, long queueId, String userId) {
        sessionQueueIdMap.put(session.getId(), new AbstractMap.SimpleEntry<>(numQueues, userId));
        trackQueues.get(queueId).addConnectedUser(userId, session);
    }

    public void addAdmin(Session session, long queueId, String userId) {
        sessionQueueIdMap.put(session.getId(), new AbstractMap.SimpleEntry<>(numQueues, userId));
        trackQueues.get(queueId).addAdmin(userId, session);
    }

    public boolean startQueue(Session session, String userId) {
        if (!queueExists(getConnectedQueueId(session))) {
            numQueues++;
            trackQueues.put(numQueues, new TrackQueue(numQueues));
            addAdmin(session, numQueues, userId);
            return true;
        }
        return false;
    }

    static class TrackQueueCommands {
        public static final String START_QUEUE = "start_queue!";
        public static final String JOIN_QUEUE = "join_queue!";
        public static final String LEAVE_QUEUE = "leave_queue!";
        public static final String ADD_SONG = "add_song!";
        public static final String DOWN_VOTE = "down_vote!";
    }


}
