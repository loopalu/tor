package server;

import Registry.Register;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientAndServer implements Runnable {

    private static int myIp;
    private ArrayList<String> neighbours = new ArrayList<>();
    private String acting;
    private Boolean isRunning;
    private String prev = "";

    ClientAndServer(int port) {
        myIp = port;
        this.isRunning = true;
    }

    public void run() {
        Client client = new Client("", "");
        ClientListener clientListener = new ClientListener(myIp);
        Register register = new Register(9000);
        while (isRunning) {
            if (!(prev.equals("Server"))) {
                try {
                    URL urlServer = new URL("http://localhost:9000");
                    HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
                    urlConn.connect();
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
                    new Thread(clientListener).start();
                } else {
                    if (!(prev.equals(""))) {
                        client.stop();
                        clientListener.stop();
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

    private void setNeighbors() throws IOException {
        URL url = new URL("http://localhost:9000");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        String body = "{\"ip\":\"" + myIp + "\",\"action\":\"Enter\"}";
        conn.setRequestProperty("Content-Type", "text/plain");
        conn.setRequestProperty("Content-Length", Integer.toString(body.length()));
        conn.setDoOutput(true);
        conn.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));

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
        this.neighbours = new ArrayList<>(Arrays.asList(arr));
    }
}
