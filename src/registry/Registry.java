package registry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Registry implements Runnable {

    private int port;
    public static ArrayList<String> listOfPeers = new ArrayList<>();
    public ServerSocket serverSocket;
    private boolean isStopped = false;

    public Registry(int portID) {
        this.port = portID;
    }

    /**
     * Start Registry to handle Clients
     */
    public void run() {
        System.out.println("Server started.");
        new Thread(new RegistryRefresher()).start();
        openServerSocket();
        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }

            new Thread(
                    new RegistryRunnable(
                            clientSocket)
            ).start();
        }
        RegistryRefresher.stop();
        System.out.println("Server Stopped.");

    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    /**
     * Stop Registry
     */
    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    /**
     * Open Registry on port
     */
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.port, e);
        }
    }

}
