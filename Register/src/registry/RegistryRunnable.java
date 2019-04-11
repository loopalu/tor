package registry;

import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class RegistryRunnable implements Runnable{

    protected Socket clientSocket;

    RegistryRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line;
            line = in.readLine();
            String message;
            String ip = "";
            String action = "";
            if (line.contains("GET") && line.contains("getpeers")){
                while ((line = in.readLine()) != null && (line.length() != 0)) {
                    if (line.contains("action:")) {
                        action = line.substring(8, line.length());
                    } else if (line.contains("ip:")) {
                        ip = line.substring(4, line.length());
                    }
                }
                if (action.equals("Enter") && !(ip.equals(""))) {
                    message = actionEnter(ip);
                } else {
                    message = "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/html\r\n" +
                            "\r\n" +
                            "Wrong data";
                }
            } else {
                message = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "\r\n" +
                        "Wrong request";
            }
            output.write(message.getBytes());
            output.close();
            input.close();
            clientSocket.close();
            long time = System.currentTimeMillis();
            System.out.println("Request processed: " + time);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Code to take 2 random ip-s from listOfIps and send them back. Wont run until at least 3 people have entered
    private String actionEnter(String ip) {
        String message;
        if (!Registry.listOfPeers.contains(ip)) {
            Registry.listOfPeers.add(ip);
        }
        while (true) {
            ArrayList<String> temporaryListOfIps = new ArrayList<>(Registry.listOfPeers);
            System.out.println(temporaryListOfIps);
            if (temporaryListOfIps.size() > 2) {
                String[] ips = new String[2];
                int n = 0;
                while (n < 2) {
                    int rnd = new Random().nextInt(temporaryListOfIps.size());
                    if (!temporaryListOfIps.get(rnd).equals(ip) && !(Arrays.asList(ips).contains(temporaryListOfIps.get(rnd)))) {
                        ips[n] = temporaryListOfIps.get(rnd);
                        n += 1;
                    }
                }
                String strOfIps = String.join(",", ips);
                JSONObject body = new JSONObject();
                body.put("ips", strOfIps);
                message = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: application/json\r\n" +
                        "\r\n" +
                        body;
                break;
            }
        }
        return message;
    }
}