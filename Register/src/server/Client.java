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

    public Client() {
        this.isRunning = true;
    }

    public void run() {

        while (isRunning) {
            Scanner reader = new Scanner(System.in);
            System.out.println("Enter page: ");
            String urlString = reader.nextLine();
            if (ClientAndServer.getNeighbours().size() >= 2){
                for (String i : ClientAndServer.getNeighbours()) {
                    String address = "http://localhost:" + ClientAndServer.getMyIp();

                    // Aivari tehtud asi, mis ei tööta.
//                    URL url = null;
//                    try {
//                        url = new URL(address);
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    }
//                    HttpURLConnection conn = null;
//                    try {
//                        assert url != null;
//                        conn = (HttpURLConnection) url.openConnection();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        assert conn != null;
//                        conn.setRequestMethod("GET");
//                    } catch (ProtocolException e) {
//                        e.printStackTrace();
//                    }
//                    conn.setRequestProperty("Content-Type", "text/plain");
//                    conn.setRequestProperty("Url", urlString);
//                    conn.setDoOutput(true);
//
//                    try {
//                        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }


                    System.out.println(i);
                }
            } else {
                System.out.println("Pole piisavalt naabreid!");
            }
        }
    }

    public synchronized void stop() {
        this.isRunning = false;
    }

    public synchronized void suspend() {
        this.isRunning = false;
    }

    public synchronized void resume() {
        this.isRunning = true;
    }


}
