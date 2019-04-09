package tester;

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

public class getNeighbors {

    private static String myIp = "192.168.123";
    public static ArrayList<String> neighbors = new ArrayList<>();
    private static String RegistryIP = "localhost:1215/getpeers";

    public static void main(String[] args) throws IOException {
        setNeighbors();
    }

    public static void setNeighbors() throws IOException {
        URL url = new URL(RegistryIP);
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
        if (ips == null) {
            return;
        }
        String ipString = (String) ips.get("ips");
        if (ipString == null) {
            return;
        }
        String[] arr = ipString.split(",");
        ArrayList<String> neighbours = new ArrayList<>(Arrays.asList(arr));
        System.out.println(neighbours);
    }

}
