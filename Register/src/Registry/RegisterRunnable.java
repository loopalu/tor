package Registry;

import org.json.simple.JSONObject;

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
            System.out.println(line);
            String message;
            String ip = "";
            String action = "";
            if (line.contains("GET") && line.contains("getpeers")){
                while ((line = in.readLine()) != null && (line.length() != 0)) {
                    if (line.contains("action:")) {
                        action = line.substring(8, line.length());
                        System.out.println(action);
                    } else if (line.contains("ip:")) {
                        ip = line.substring(4, line.length());
                        System.out.println(ip);
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
        if (!Register.listOfPeers.contains(ip)) {
            Register.listOfPeers.add(ip);
        }
        while (true) {
            System.out.println(Register.listOfPeers);
            if (Register.listOfPeers.size() > 2) {
                String[] ips = new String[2];
                int n = 0;
                while (n < 2) {
                    int rnd = new Random().nextInt(Register.listOfPeers.size());
                    if (!Register.listOfPeers.get(rnd).equals(ip) && !(Arrays.asList(ips).contains(Register.listOfPeers.get(rnd)))) {
                        ips[n] = Register.listOfPeers.get(rnd);
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