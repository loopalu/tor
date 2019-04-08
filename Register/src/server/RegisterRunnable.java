package server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;


public class RegisterRunnable implements Runnable{

    protected Socket clientSocket;

    public RegisterRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line;
            line = in.readLine();
            line = "";

            // looks for post data
            int postDataI = -1;
            while ((line = in.readLine()) != null && (line.length() != 0)) {
                if (line.contains("Content-Length:")) {
                    postDataI = new Integer(
                            line.substring(
                                    line.indexOf("Content-Length:") + 16,
                                    line.length()));
                }
            }
            String postData = null;

            // read the post data
            if (postDataI > 0) {
                char[] charArray = new char[postDataI];
                in.read(charArray, 0, postDataI);
                postData = new String(charArray);
            }

            //On false entry sends back a message and closes thread
            if (postData == null) {
                String message = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "\r\n" +
                        "No data was sent";
                output.write(message.getBytes());
                output.close();
                input.close();
                clientSocket.close();
                return;
            }

            JSONParser parser = new JSONParser();
            JSONObject test = null;
            try {
                test = (JSONObject)parser.parse(postData);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String actionNeeded = (String) test.get("action");
            String ip = (String) test.get("ip");
            String message;

            //Compiles a message to send back based on information gotten from the body of message received.
            if (actionNeeded.equals("Enter") && test.get("ip") != null ) {
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
            clientSocket.close();
            long time = System.currentTimeMillis();
            System.out.println("Request processed: " + time);
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    //Code to take 2 random ip-s from listOfIps and send them back. Wont run until at least 3 people have entered
    private String actionEnter(String ip) {
        String message;
        if (!Register.listOfPeers.existsPeer(ip)) {
            Register.listOfPeers.addPeers(ip);
        }
        while (true) {
            System.out.println(Register.listOfPeers);
            if (Register.listOfPeers.getPeers().size() > 2) {
                String[] ips = new String[2];
                int n = 0;
                while (n < 2) {
                    int rnd = new Random().nextInt(Register.listOfPeers.getPeers().size());
                    if (!Register.listOfPeers.getPeers().get(rnd).equals(ip) && !(Arrays.asList(ips).contains(Register.listOfPeers.getPeers().get(rnd)))) {
                        ips[n] = Register.listOfPeers.getPeers().get(rnd);
                        n += 1;
                    }
                }
                String str = String.join(",", ips);
                JSONObject body = new JSONObject();
                body.put("ips",str);
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