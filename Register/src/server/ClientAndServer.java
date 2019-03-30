package server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientAndServer implements Runnable {

    public static int myIp;
    public static ArrayList<String> neighbours = new ArrayList<>();
    public String acting;
    public Boolean isRunning;
    public String prev = "";

    public ClientAndServer(int port) {
        myIp = port;
        this.isRunning = true;
    }

    public void run() {
        Client client = new Client();
        Register register = new Register(9000);
        while (isRunning) {
            if (!(prev.equals("Server"))) {
                try {
                    URL urlServer = new URL("http://localhost:9000");
                    HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
                    urlConn.connect();
                    System.out.println("Here");
                    if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        acting = "Client";
                    } else {
                        acting = "Server";
                    }
                } catch (IOException e) {
                    acting = "Server";
                }
            }
            if (!(prev.equals(acting))){
                if (acting.equals("Client")) {
                    if (!(prev.equals(""))) {
                        register.stop();
                    }
                    new Thread(client).start();
                } else {
                    if (!(prev.equals(""))) {
                        client.stop();
                    }
                    new Thread(register).start();
                }
            }
            if (acting.equals("Client")) {
                try {
                    setNeighbors();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.prev = acting;
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getMyIp() {
        return myIp;
    }

    public void setNeighbors() throws IOException {
        URL url = new URL("http://localhost:9000");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        String body = "{\"ip\":\"" + myIp + "\",\"action\":\"Enter\"}";
        conn.setRequestProperty("Content-Type", "text/plain");
        conn.setRequestProperty("Content-Length", Integer.toString(body.length()));
        conn.setDoOutput(true);
        conn.getOutputStream().write(body.getBytes("UTF8"));

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        StringBuilder str = new StringBuilder();
        for (int c; (c = in.read()) >= 0;) {
            System.out.print((char) c);
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
        System.out.println(ipString);
        String[] arr = ipString.split(",");
        neighbours = new ArrayList<>(Arrays.asList(arr));
    }

    public static ArrayList<String> getNeighbours(){
        return neighbours;
    }
}
