package server;

import util.FileWritter;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class Client implements Runnable{

    private boolean isRunning;
    private Integer ip;

    Client(int myIp) {
        this.isRunning = true;
        this.ip = myIp;
    }

    public void run() {
        System.out.println("Client started");
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
                FileWritter.write(ip,time);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ClientAndServer.getNeighbours().size() >= 2){
                for (String i : ClientAndServer.getNeighbours()) {
                    try {
                        URL url = new URL("http://localhost:"+i);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "text/plain");
                        conn.setRequestProperty("veebiaadress", urlString);
                        conn.setRequestProperty("messageid",time);
                        conn.setRequestProperty("timetolive", String.valueOf(10));
                        conn.setRequestProperty("ip", String.valueOf(ip));
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
