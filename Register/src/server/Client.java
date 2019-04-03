package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class Client implements Runnable{

    public boolean isRunning;
    public ArrayList<String> myRequests = new ArrayList<>();

    public Client() {
        this.isRunning = true;
    }

    public void run() {

        while (isRunning) {
            Scanner reader = new Scanner(System.in);
            System.out.println("Enter page: ");
            String urlString = reader.nextLine();
            String time = String.valueOf(System.currentTimeMillis());
            myRequests.add(time);
            if (ClientAndServer.getNeighbours().size() >= 2){
                for (String i : ClientAndServer.getNeighbours()) {
                    try {
                        System.out.println(i);
                        URL url = new URL("http://localhost:"+i);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "text/plain");
                        conn.setRequestProperty("veebiaadress", urlString);
                        conn.setRequestProperty("messageid",time);
                        conn.setRequestProperty("timetolive", String.valueOf(10));
                        conn.setDoOutput(true);
                        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("Pole piisavalt naabreid!");
            }
        }
    }

    public synchronized void stop() {
        this.isRunning = false;
    }

    public ArrayList<String> getMyRequests() {
        return myRequests;
    }
}
