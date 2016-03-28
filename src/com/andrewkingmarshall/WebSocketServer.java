package com.andrewkingmarshall;

import org.glassfish.tyrus.server.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by jfowler on 3/28/16.
 */
public class WebSocketServer {

    public static final int PORT = 7591;
    public static final String ADDRESS = "ec2-52-90-175-243.compute-1.amazonaws.com";

    public static void runServer() {
        Server server = new Server(ADDRESS, PORT, "", null, WebSocketEndPoint.class);
        try {
            server.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Please press a key to stop the server.");
            reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
