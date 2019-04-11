package registry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;


public class RegisterRunnable implements Runnable {

    protected Socket clientSocket;

    RegisterRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line;
            line = in.readLine();
            String message;
            String ip = "";
            String action = "";
            if (line.contains("GET") && line.contains("getpeers")) {
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

    /**
     * Code to take 2 random ip-s from listOfIps and send them back. Wont run until at least 3 people have entered
     *
     * @param ip
     * @return
     */
    private String actionEnter(String ip) {
        String message;
        if (!Register.listOfPeers.contains(ip)) {
            Register.listOfPeers.add(ip);
        }

        while (true) {
            ArrayList<String> temporaryListOfIps = new ArrayList<>(Register.listOfPeers);
            System.out.println(temporaryListOfIps);
            if (temporaryListOfIps.size() > 2) {
                JSONArray ips = new JSONArray();
                while (ips.size() < 2) {
                    String randomIp = temporaryListOfIps.get(new Random().nextInt(temporaryListOfIps.size()));
                    if (!(randomIp.equals(ip)) && !(ips.contains(randomIp))) {
                        ips.add(randomIp);
                    }
                }
                JSONObject body = new JSONObject();
                body.put("ips", ips);
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