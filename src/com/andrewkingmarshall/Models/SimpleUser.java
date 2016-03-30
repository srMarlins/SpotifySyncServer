package com.andrewkingmarshall.Models;

/**
 * Created by jfowler on 3/30/16.
 */
public class SimpleUser {
    private String userId;
    private long queueId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getQueueId() {
        return queueId;
    }

    public void setQueueId(long queueId) {
        this.queueId = queueId;
    }
}
