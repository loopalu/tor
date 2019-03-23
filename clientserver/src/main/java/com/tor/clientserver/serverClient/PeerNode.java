package com.tor.clientserver.serverClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.tor.clientserver.serverClient.HttpRequestHandler;

public class PeerNode {
    private int port;

    PeerNode(int port) {
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

                System.out.println("Thread started for " + port);

                if (HttpRequestHandler.listOfIps.size() == 3) {
                    server.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
