package com.andrewkingmarshall;

import com.andrewkingmarshall.Models.TestSongListModel;
import com.andrewkingmarshall.Models.Track;
import com.google.gson.Gson;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class QueueServerHandler extends IoHandlerAdapter {

    public static final int PORT = 7591;

    private TestSongListModel testSongListModel = new TestSongListModel();

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String str = message.toString();

        System.out.println(str);

        if (str.trim().contains("exit")) {
            session.close(true);
            return;
        }

        Gson gson = new Gson();

        Track incomingTrack = gson.fromJson(str, Track.class);
        testSongListModel.addSong(incomingTrack);
        session.write(gson.toJson(testSongListModel));
        session.close(true);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("IDLE " + session.getIdleCount(status));
    }
}