package server;

import java.io.IOException;

public class AskNewNeighbors implements Runnable {

    private static boolean isRunning = true;

    public static void stop() {
        isRunning = false;
    }

    public void run() {
        try {
            while (isRunning) {
                Client.setNeighbors();
                Thread.sleep(60 * 1000);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
