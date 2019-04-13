package node;

import util.FileWritter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Sender implements Runnable {

    private boolean isRunning = true;

    /**
     *
     */
    public void run() {
        while (isRunning) {
            Scanner reader = new Scanner(System.in);
            System.out.println("Enter page: ");
            String urlString = null;
            try {
                urlString = URLEncoder.encode(reader.nextLine(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String time = String.valueOf(System.currentTimeMillis());
            try {
                FileWritter.write(String.valueOf(Client.getPort()), time);
            } catch (IOException e) {
                e.printStackTrace();
            }
           if (Client.getNeighbours().size() >= 2) {
                for (String i : Client.getNeighbours()) {
                    try {
                        URL url = new URL(i);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "text/plain");
                        conn.setRequestProperty("url", urlString);
                        conn.setRequestProperty("id",time);
                        conn.setRequestProperty("timetolive", String.valueOf(10));
                        conn.setRequestProperty("ip", String.valueOf(Client.getMyIp()));
                        conn.setDoOutput(true);
                        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
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

    synchronized void stop() {
        this.isRunning = false;
    }
}
