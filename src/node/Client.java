package node;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client implements Runnable{

    private String registryIP;
    private boolean isRunning;
    private static String myIp;
    private static ArrayList<String> neighbors = new ArrayList<>();
    private static Integer port;

    Client(String myIp, String registryIp, Integer port) {
        this.isRunning = true;
        this.myIp = myIp + ":" + port;
        this.registryIP = registryIp;
        this.port = port;
    }

    public static ArrayList<String> getNeighbours() {
        return neighbors;
    }

    public void run() {
        System.out.println("Client started");
        ClientListener clientListener = new ClientListener(port);
        new Thread(clientListener).start();
        Sender sender = new Sender();
        new Thread(sender).start();
        while (isRunning) {
            try {
                setNeighbors();
                Thread.sleep(600 * 1000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        clientListener.stop();
        sender.stop();
    }

    synchronized void stop() {
        this.isRunning = false;
    }

    private void setNeighbors() throws IOException {
        URL url = new URL(this.registryIP + "/getpeers");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("ip", myIp);
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

        JSONArray ipString = (JSONArray) ips.get("ips");
        neighbors = new ArrayList<>(ipString);
    }

    public static Integer getPort() {
        return port;
    }

    public static String getMyIp() {
        return myIp;
    }
}