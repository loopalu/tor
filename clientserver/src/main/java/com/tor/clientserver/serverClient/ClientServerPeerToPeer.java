package com.tor.clientserver.serverClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.tor.clientserver.serverClient.HttpRequestHandler;

public class ClientServerPeerToPeer {
    private int port;

    public ClientServerPeerToPeer(int port) {
        this.port = port;

        new Thread(new Runnable() {
            @Override
            public void run() {
                startClientServer();
            }
        }).start();
    }


    private void startClientServer() {


        try {
            URL url = new URL("http://localhost:9000/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            int statusCode = http.getResponseCode();
            if (statusCode == 200) {
                port = ThreadLocalRandom.current().nextInt(7000, 8000);
            }
        } catch (IOException e) {
            System.out.println("Creating server");
        }


        try {
            // Establish the listen socket.
            ServerSocket server = new ServerSocket(port);
            System.out.println("listening on port " + server.getLocalPort());

            while (true) {
                // Listen for a TCP connection request.
                Socket connection = server.accept();

                // Construct an object to process the HTTP request message.
                HttpRequestHandler request = new HttpRequestHandler(connection);

                // Create a new thread to process the request.
                Thread thread = new Thread(request);

                // Start the thread.
                thread.start();

                System.out.println("Thread started for " + connection.getLocalPort());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
