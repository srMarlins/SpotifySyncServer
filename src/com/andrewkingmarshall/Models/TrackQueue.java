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
    private Hashtable<String, String> connectedAdminList = new Hashtable<>();
    private Hashtable<String, String> connectedUserList = new Hashtable<>();

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

    public Hashtable<String, String> getConnectedUserList() {
        return connectedUserList;
    }

    public void setConnectedUserList(Hashtable<String, String> connectedUserList) {
        this.connectedUserList = connectedUserList;
    }

    public void addConnectedUser(String userId, String session) {
        connectedUserList.put(userId, session);
    }

    public void removeConnectedUser(String userId) {
        connectedUserList.remove(userId);
    }

    public Hashtable<String, String> getConnectedAdminList() {
        return connectedAdminList;
    }

    public void setConnectedAdminList(Hashtable<String, String> connectedAdminList) {
        this.connectedAdminList = connectedAdminList;
    }

    public void addAdmin(String userId, String session) {
        connectedAdminList.put(userId, session);
    }

    public void removeAdmin(String userId) {
        connectedAdminList.remove(userId);
    }

    public Hashtable<String, String> getAllConnectedUsers() {
        Hashtable<String, String> map = new Hashtable<>();
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

    public void setQueueId(long queueId) {
        this.queueId = queueId;
    }
}
