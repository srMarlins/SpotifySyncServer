package com.andrewkingmarshall.Utils;


import com.andrewkingmarshall.Models.SpoqModel;
import com.andrewkingmarshall.Models.SpoqUser;
import com.google.gson.Gson;

import javax.websocket.Session;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;

/**
 * Created by jfowler on 3/30/16.
 */
public class SessionHandler {

    private static SessionHandler sessionHandler;
    /**
     * A map of the user's sessionId to a key value pair containg the User's musicServiceId and the session itself
     */
    private HashMap<String, AbstractMap.SimpleEntry<SpoqUser,Session>> sessionMap = new HashMap<>();


    public static SessionHandler getInstance(){
        if(sessionHandler == null){
            sessionHandler = new SessionHandler();
        }
        return sessionHandler;
    }

    public void addSession(Session session, SpoqUser user){
        System.out.println("Session added: " + user.getConnectedPlaylistId() + "  :  " +  session.getId());
        sessionMap.put(session.getId(), new AbstractMap.SimpleEntry<>(user, session));
    }

    public void removeSession(String sessionId){
        System.out.println("Removing session : " + sessionId);
        sessionMap.remove(sessionId);
    }

    public Session getSession(String sessionId){
        AbstractMap.SimpleEntry<SpoqUser, Session> sessionPair =  sessionMap.get(sessionId);
        if(sessionPair != null){
            System.out.println("playlistId: " + ((sessionPair.getKey() == null) ? "null" : sessionPair.getKey().getConnectedPlaylistId()));
            System.out.println("sessionId: " + ((sessionPair.getValue() == null) ? "null" : sessionPair.getValue().getId()));
            System.out.println("Retrieving session: " + sessionPair.getKey().getConnectedPlaylistId() + "  :  " +  sessionPair.getValue().getId());
            return sessionPair.getValue();
        }
        System.out.println("Session: " + sessionId + " couldn't be found");
        return null;
    }

    public SpoqUser getUserFromSessionId(String sessionId){
        AbstractMap.SimpleEntry<SpoqUser, Session> sessionPair =  sessionMap.get(sessionId);
        if(sessionPair != null){
            return sessionPair.getKey();
        }
        return null;
    }

    public void broadcastSpoqUpdate(SpoqModel spoqModel) {
        for (SpoqUser user : spoqModel.getUserMap().values()) {
            System.out.println("Broadcasting to: " + user.getSessionId());
            for(String sesId : sessionMap.keySet()){
                System.out.println("Session id in map: " + sesId);
            }
            Session session = getSession(user.getSessionId());
            try {
                Gson gson = new Gson();
                String jsonMessage = gson.toJson(spoqModel);
                session.getBasicRemote().sendText(jsonMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
