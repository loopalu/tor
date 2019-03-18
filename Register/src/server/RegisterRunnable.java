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
    protected String serverText;

    public RegisterRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line;
            line = in.readLine();
            System.out.println("HTTP-HEADER: " + line);
            line = "";
            // looks for post data
            int postDataI = -1;
            while ((line = in.readLine()) != null && (line.length() != 0)) {
                System.out.println("HTTP-HEADER: " + line);
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
            System.out.println(postData);

            if (postData == null) {
                String message = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "\r\n" +
                        "No data was sent";
                output.write(message.getBytes());
                output.close();
                input.close();
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
            System.out.println(actionNeeded);
            System.out.println(ip);
            String message;
            if (actionNeeded.equals("Enter") && test.get("ip") != null ) {
                message = actionEnter(ip);
            } else if (actionNeeded.equals("Leave") && (test.get("LeaverIp") != null) && (test.get("PairedIp") != null)){
                message = actionLeave(ip,(String) test.get("LeaverIp"), (String) test.get("PairedIp"));
            } else if (actionNeeded.equals("NewPairs") && (test.get("OldPair1") != null) && (test.get("OldPair2") != null)) {
                Register.listOfIps.remove((String) test.get("OldPair1"));
                Register.listOfIps.remove((String) test.get("OldPair2"));
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
            clientSocket = null;
            serverText = null;
            long time = System.currentTimeMillis();
            System.out.println("Request processed: " + time);
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }

    private String actionEnter(String ip) {
        String message;
        if (!Register.listOfIps.contains(ip)) {
            Register.listOfIps.add(ip);
        }
        while (true) {
            System.out.println(Register.listOfIps);
            if (Register.listOfIps.size() > 2) {
                String[] ips = new String[2];
                int n = 0;
                while (n < 2) {
                    int rnd = new Random().nextInt(Register.listOfIps.size());
                    if (!Register.listOfIps.get(rnd).equals(ip) && !(Arrays.asList(ips).contains(Register.listOfIps.get(rnd)))) {
                        ips[n] = Register.listOfIps.get(rnd);
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

    private String actionLeave(String ip, String leaverIp, String pairIp) {
        String message;
        Register.listOfIps.remove(leaverIp);
        while (true) {
            System.out.println(Register.listOfIps);
            if (Register.listOfIps.size() > 2) {
                String[] ips = new String[Register.listOfIps.size() - 2];
                int n = 0;
                for (String i : Register.listOfIps) {
                    if (!i.equals(ip) && !i.equals(pairIp)) {
                        ips[n] = i;
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