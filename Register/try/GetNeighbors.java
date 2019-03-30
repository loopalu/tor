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


public class GetNeighbors {
    public static String myIp = "7000";
    public static ArrayList<String> neighborgIps = new ArrayList<>();


    public static void main(String[] args) {
        try {
            while (true) {
                makeRequestToServer();
                Thread.sleep(60 * 1000);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }


    public static void makeRequestToServer() throws IOException {
        URL url = new URL("http://localhost:9000");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        String body = "{\"ip\":\"" + myIp + "\",\"action\":\"Enter\"}";
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/plain");
        conn.setRequestProperty("Content-Length", Integer.toString(body.length()));
        conn.setDoOutput(true);
        conn.getOutputStream().write(body.getBytes("UTF8"));

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

    }
}
