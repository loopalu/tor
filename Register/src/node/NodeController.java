package node;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class NodeController implements Runnable{

    private String registryIP;
    private boolean isRunning;
    private static String myIp;
    private static ArrayList<String> neighbors = new ArrayList<>();
    private static Integer port;

    NodeController(String myIp, String registryIp, Integer port) {
        this.isRunning = true;
        this.myIp = myIp + ":" + port;
        this.registryIP = registryIp;
        this.port = port;
    }

    static ArrayList<String> getNeighbours() {
        return neighbors;
    }

    public void run() {
        System.out.println("NodeController started");
        NodeListener nodeListener = new NodeListener(port);
        new Thread(nodeListener).start();
        PrimaryMessage primaryMessage = new PrimaryMessage();
        new Thread(primaryMessage).start();
        while (isRunning) {
            try {
                setNeighbors();
                Thread.sleep(600 * 1000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        nodeListener.stop();
        primaryMessage.stop();
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

        StringBuilder neighborString = new StringBuilder();
        for (int c; (c = in.read()) >= 0;) {
            neighborString.append((char) c);
        }
        String outputString = neighborString.toString();
        JSONParser parser = new JSONParser();
        JSONObject ips = null;
        try {
            ips = (JSONObject)parser.parse(outputString);
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
        String[] arrayOfNeigbors = ipString.split(",");
        neighbors = new ArrayList<>(Arrays.asList(arrayOfNeigbors));
    }

    static Integer getPort() {
        return port;
    }

    static String getMyIp() {
        return myIp;
    }
}
