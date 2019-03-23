package com.tor.clientserver.serverClient;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class HttpRequestHandler implements Runnable {
    Socket socket;
    public static ArrayList<String> listOfIps = new ArrayList<>();

    public HttpRequestHandler(Socket socket) throws Exception {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /*
     * Gets a request from another node.
     * Sends the file to the node if available.
     */
    private void processRequest() throws Exception {
        try {
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line;
            line = in.readLine();
            System.out.println(line);
            System.out.println("HTTP-HEADER: " + line);
            line = "";

            // looks for post data
            int postDataI = -1;
            while ((line = in.readLine()) != null && (line.length() != 0)) {
                System.out.println("HTTP-HEADER: " + line);
                if (line.contains("Data: message")) {
                    String message = "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/html\r\n" +
                            "\r\n" +
                            "Message received\n";
                    output.write(message.getBytes());
                    output.close();
                    input.close();
                    socket = null;
                    return;

                }
                if (line.contains("Content-Length:")) {
                    postDataI = new Integer(
                            line.substring(
                                    line.indexOf("Content-Length:") + 16,
                                    line.length()));
                }
            }

            //POST /download HTTP/1.1

            String postData = null;

            // read the post data
            if (postDataI > 0) {
                char[] charArray = new char[postDataI];
                in.read(charArray, 0, postDataI);
                postData = new String(charArray);
            }
            System.out.println(postData);

            //On false entry sends back a message and closes thread
            if (postData == null) {
                String message = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "\r\n" +
                        "No data was sent";
                output.write(message.getBytes());
                output.close();
                input.close();
                socket = null;
                return;
            }

            JSONParser parser = new JSONParser();
            JSONObject test = null;
            try {
                test = (JSONObject) parser.parse(postData);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String ip = (String) test.get("ip");
            String message;

            //Compiles a message to send back based on information gotten from the body of message received.
            if (test.get("ip") != null) {
                message = actionEnter(ip);
            } else {
                message = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "\r\n" +
                        "???";
            }

            output.write(message.getBytes());

            output.close();
            input.close();

            socket = null;
            long time = System.currentTimeMillis();
            System.out.println("Request processed: " + time);

        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }


    //Code to take 2 random ip-s from listOfIps and send them back. Wont run until at least 3 people have entered
    private String actionEnter(String ip) {
        String message;
        if (!listOfIps.contains(ip)) {
            listOfIps.add(ip);
        }
        while (true) {
            System.out.println(listOfIps);
            if (listOfIps.size() > 2) {
                String[] ips = new String[2];
                int n = 0;
                while (n < 2) {
                    int rnd = new Random().nextInt(listOfIps.size());
                    if (!listOfIps.get(rnd).equals(ip) && !(Arrays.asList(ips).contains(listOfIps.get(rnd)))) {
                        ips[n] = listOfIps.get(rnd);
                        n += 1;
                    }
                }
                String str = String.join(",", ips);
                JSONObject body = new JSONObject();
                body.put("ips", str);
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
