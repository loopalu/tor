package Registry;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Timer implements Runnable{

    private static boolean isRunning = true;

    public static void stop() {
        isRunning = false;
    }

    //Every 60 seconds activates serviceRemove function
    public void run() {
        try {
            while (isRunning) {
                serviceRemove();
                Thread.sleep(60 * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Function to check all the ips in listOfIps and ping them to see if they there, if not deletes them from list
    private void serviceRemove() {
        String str;
        ArrayList<String> arr = new ArrayList<>(Register.listOfPeers.getPeers());
        for (String i : arr) {
            System.out.println(i);
            if (i.length() <= 5) {
                str = "http://localhost:" + i + "/";
            } else {
                str = "http://" + i + ":1215/";
            }
            try {
                URL urlServer = new URL(str);
                HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
                urlConn.connect();
                if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    continue;
                } else {
                    Register.listOfPeers.removePeer(i);
                }
            } catch (IOException e) {
                Register.listOfPeers.removePeer(i);
            }
        }

    }
}
