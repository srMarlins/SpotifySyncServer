package com.andrewkingmarshall;


import javax.websocket.Session;
import java.util.HashMap;

/**
 * Created by jfowler on 3/30/16.
 */
public class SessionHandler {
    HashMap<String, Session> sessionMap;

    public SessionHandler(){
        sessionMap = new HashMap<>();
    }

    public void addSession(Session session){
        sessionMap.put(session.getId(), session);
    }

    public Session getSession(String sessionId){
        return sessionMap.get(sessionId);
    }
}
