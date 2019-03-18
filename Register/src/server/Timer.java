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
            if (i.length() <= 5) {
                str = "http://localhost:" + i + "/";
            } else {
                str = "http://" + i + "/";
            }
            System.out.println(str);
            try {
                URL urlServer = new URL("your server url");
                HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
                urlConn.setConnectTimeout(1000); //<- 3Seconds Timeout
                urlConn.connect();
                if (urlConn.getResponseCode() == 200) {
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
