package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Timer implements Runnable{


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
                urlConn.setConnectTimeout(1000); //<- 3Seconds Timeout
                urlConn.connect();
                if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    continue;
                } else {
                    Register.listOfIps.remove(i);
                }
            } catch (IOException e) {
                System.out.println(e);
                Register.listOfIps.remove(i);
            }
        }

    }
}
