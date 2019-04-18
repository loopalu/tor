package node;

import util.FileWritter;
import util.PropertyReader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class NodeSender implements Runnable {

    private boolean isRunning = true;

    /**
     * Run NodeSender and start asking client what to send to neighbours
     */
    public void run() {
        while (isRunning) {
            PropertyReader propertyReader;
            try {
                int port = NodeController.getPort();
                propertyReader = new PropertyReader(port + "config.properties");
                int timeToLive = propertyReader.getTimeToLive();
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
                    FileWritter.write(String.valueOf(port), time, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (NodeController.getNeighbours().size() >= 2) {
                    for (String i : NodeController.getNeighbours()) {
                        try {
                            URL url = new URL(i);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                            conn.setRequestMethod("GET");
                            conn.setRequestProperty("Content-Type", "text/plain");
                            conn.setRequestProperty("url", urlString);
                            conn.setRequestProperty("id", time);
                            conn.setRequestProperty("timetolive", String.valueOf(timeToLive));
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized void stop() {
        this.isRunning = false;
    }
}
