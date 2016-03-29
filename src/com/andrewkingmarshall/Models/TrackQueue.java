package com.andrewkingmarshall.Models;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by jfowler on 3/29/16.
 */
public class TrackQueue {
    private long queueId;
    private List<String> trackIdList = new ArrayList<>();
    private Hashtable<String, Session> connectedAdminList = new Hashtable<>();
    private Hashtable<String, Session> connectedUserList = new Hashtable<>();

    public TrackQueue(long uniqueQueueId) {
        queueId = uniqueQueueId;
    }

    public List<String> getTrackIdList() {
        return trackIdList;
    }

    public void setTrackIdList(List<String> trackIdList) {
        this.trackIdList = trackIdList;
    }

    public void addTrackId(String trackId) {
        trackIdList.add(trackId);
    }

    public void removeTrackId(String trackId) {
        trackIdList.remove(trackId);
    }

    public Hashtable<String, Session> getConnectedUserList() {
        return connectedUserList;
    }

    public void setConnectedUserList(Hashtable<String, Session> connectedUserList) {
        this.connectedUserList = connectedUserList;
    }

    public void addConnectedUser(String userId, Session session) {
        connectedUserList.put(userId, session);
    }

    public void removeConnectedUser(String userId) {
        connectedUserList.remove(userId);
    }

    public Hashtable<String, Session> getConnectedAdminList() {
        return connectedAdminList;
    }

    public void setConnectedAdminList(Hashtable<String, Session> connectedAdminList) {
        this.connectedAdminList = connectedAdminList;
    }

    public void addAdmin(String userId, Session session) {
        connectedAdminList.put(userId, session);
    }

    public void removeAdmin(String userId) {
        connectedAdminList.remove(userId);
    }

    public Hashtable<String, Session> getAllConnectedUsers() {
        Hashtable<String, Session> map = new Hashtable<>();
        map.putAll(connectedUserList);
        map.putAll(connectedAdminList);
        return map;
    }

    public void removeGenericUser(String userId) {
        removeAdmin(userId);
        removeConnectedUser(userId);
    }

    public long getQueueId() {
        return queueId;
    }
}
