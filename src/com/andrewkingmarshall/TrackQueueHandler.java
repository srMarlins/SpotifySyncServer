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
    private SessionHandler sessionHandler;

    public TrackQueueHandler() {
        trackQueues = new HashMap<>();
        sessionQueueIdMap = new HashMap<>();
        sessionHandler = new SessionHandler();
    }

    public void broadcastQueueUpdate(TrackQueue trackQueue) {
        for (String sessionId : trackQueue.getAllConnectedUsers().values()) {
            Session session = sessionHandler.getSession(sessionId);
            try {
                Gson gson = new Gson();
                String jsonMessage = gson.toJson(trackQueue);
                session.getBasicRemote().sendText(jsonMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public TrackQueue getConnectedQueue(Session session) {
        long queueId = getConnectedQueueId(session);
        return trackQueues.get(queueId);
    }

    public boolean queueExists(long queueId) {
        return trackQueues.get(queueId) != null;
    }

    public long getConnectedQueueId(Session session) {
        AbstractMap.SimpleEntry<Long, String> holder = sessionQueueIdMap.get(session.getId());
        if(holder == null){
            System.out.println("KV pair is null and session id is " + session.getId());
        }
        long queueId = holder.getKey();
        System.out.println("Queueid: " + queueId);
        return queueId;
    }

    public void removeUserFromQueue(Session session) {
        if(session != null && sessionQueueIdMap.containsKey(session.getId())) {
            String userId = sessionQueueIdMap.get(session.getId()).getValue();
            long queueId = getConnectedQueueId(session);
            trackQueues.get(queueId).removeGenericUser(userId);
            sessionQueueIdMap.remove(session.getId());
            System.out.println("User removed: " + userId);
        }
    }

    public void addUser(Session session, long queueId, String userId) {
        System.out.println("Inserting session to map: " + session.getId());
        sessionQueueIdMap.put(session.getId(), new AbstractMap.SimpleEntry<>(numQueues, userId));
        sessionHandler.addSession(session);
        trackQueues.get(queueId).addConnectedUser(userId, session.getId());
        System.out.println("User added: " + userId);
    }

    public void addAdmin(Session session, long queueId, String userId) {
        System.out.println("Inserting session to map: " + session.getId());
        sessionQueueIdMap.put(session.getId(), new AbstractMap.SimpleEntry<>(numQueues, userId));
        AbstractMap.SimpleEntry<Long, String> holder = sessionQueueIdMap.get(session.getId());
        if(holder == null){
            System.out.println("Add admin KV is null");
        }else{
            if(holder.getKey() == null) {
                System.out.println("Add admin holder's key is null");
            }
        }
        sessionHandler.addSession(session);
        trackQueues.get(queueId).addAdmin(userId, session.getId());
        System.out.println("Admin added: " + userId);
    }

    public void startQueue(Session session, String userId) {
        removeUserFromQueue(session);
        numQueues++;
        TrackQueue newTrackQueue = new TrackQueue();
        newTrackQueue.setQueueId(numQueues);
        trackQueues.put(numQueues, new TrackQueue());
        addAdmin(session, numQueues, userId);
        System.out.println("Queue created: " + numQueues);
    }

    static class TrackQueueCommands {
        public static final String START_QUEUE = "start_queue!";
        public static final String JOIN_QUEUE = "join_queue!";
        public static final String LEAVE_QUEUE = "leave_queue!";
        public static final String ADD_SONG = "add_song!";
        public static final String DOWN_VOTE = "down_vote!";
    }


}
