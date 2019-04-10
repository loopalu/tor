package server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.FileWritter;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Client implements Runnable{

    private String registryIP;
    private boolean isRunning;
    private String myIp;
    private static ArrayList<String> neighbors = new ArrayList<>();

    Client(String myIp, String registryIp) {
        this.isRunning = true;
        this.myIp = myIp;
        this.registryIP = registryIp;
    }

    public static ArrayList<String> getNeighbours() {
        return neighbors;
    }

    public void run() {
        System.out.println("Client started");
        makeRequest();
        //ClientListner Siit Toole
        try {
            setNeighbors();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isRunning) {
            try {
                setNeighbors();
                Thread.sleep(60 * 1000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    synchronized void stop() {
        this.isRunning = false;
    }

    private void setNeighbors() throws IOException {
        URL url = new URL(this.registryIP + "/getpeers");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("ip", this.myIp);
        conn.setRequestProperty("action", "Enter");
        conn.setDoOutput(true);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

        StringBuilder str = new StringBuilder();
        for (int c; (c = in.read()) >= 0;) {
            str.append((char) c);
        }
        String finString = str.toString();
        JSONParser parser = new JSONParser();
        JSONObject ips = null;
        try {
            ips = (JSONObject)parser.parse(finString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (ips == null) {
            return;
        }
        String ipString = (String) ips.get("ips");
        if (ipString == null) {
            return;
        }
        String[] arr = ipString.split(",");
        neighbors = new ArrayList<>(Arrays.asList(arr));
    }

    private void makeRequest() {
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
                FileWritter.write(this.myIp, time);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (neighbors.size() >= 2) {
                for (String i : neighbors) {
                    try {
                        URL url = new URL(i);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "text/plain");
                        conn.setRequestProperty("url", urlString);
                        conn.setRequestProperty("id", time);
                        conn.setRequestProperty("timetolive", String.valueOf(10));
                        conn.setRequestProperty("ip", String.valueOf(this.myIp));
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
}
