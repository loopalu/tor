package registry;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Timer implements Runnable {

    private static boolean isRunning = true;

    static void stop() {
        isRunning = false;
    }

    /**
     * Every 60 seconds activates serviceRemove function
     */
    public void run() {
        try {
            while (isRunning) {
                serviceRemove();
                Thread.sleep(600 * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to check all the ips in listOfIps and ping them to see if they there, if not deletes them from list
     */
    private void serviceRemove() {
        String url;
        ArrayList<String> copyOfListOfPeers = new ArrayList<>(Register.listOfPeers);
        for (String peer : copyOfListOfPeers) {
            if (peer.length() <= 5) {
                url = "http://localhost:" + peer + "/";
            } else {
                url = peer;
            }
            try {
                URL urlServer = new URL(url);
                HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();

                urlConn.connect();
                if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    continue;
                } else {
                    Register.listOfPeers.remove(peer);
                }
            } catch (IOException e) {
                Register.listOfPeers.remove(peer);
            }
        }

    }
}