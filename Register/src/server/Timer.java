package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Timer implements Runnable{

    //Every 6 seconds activates serviceRemove function
    public void run() {
        try {
            while (true) {
                serviceRemove();
                Thread.sleep(60 * 1000);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    //Function to check all the ips in listOfIps and ping them to see if they there, if not deletes them from list
    private void serviceRemove() throws IOException {
        String str;
        ArrayList<String> arr = new ArrayList<>(Register.listOfIps);
        for (String i : arr) {
            System.out.println(i);
            if (i.length() <= 5) {
                str = "http://localhost:" + i + "/";
            } else {
                str = "http://" + i + "/";
            }
            try {
                URL urlServer = new URL(str);
                HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
                //Timeout 1s
                urlConn.setConnectTimeout(1000);
                urlConn.connect();
                if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    continue;
                } else {
                    Register.listOfIps.remove(i);
                }
            } catch (IOException e) {
                Register.listOfIps.remove(i);
            }
        }

    }
}
