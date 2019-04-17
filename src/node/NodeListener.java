package node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NodeListener implements Runnable {

    private int port;
    private boolean isRunning = true;
    private ServerSocket serverSocket;

    NodeListener(int port) {
        this.port = port;
    }


    /**
     * Run NodeListener on port
     */
    public void run() {
        openServerSocket();
        while (isRunning) {
            Socket clientSocket;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isRunning) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            new Thread(new SenderReceiver(port, clientSocket)).start();
        }
        System.out.println("Server Stopped.");
    }

    synchronized void stop() {
        this.isRunning = false;
    }

    /**
     * Check if port is available and set port
     */
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.port, 20);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.port, e);
        }

    }
}
