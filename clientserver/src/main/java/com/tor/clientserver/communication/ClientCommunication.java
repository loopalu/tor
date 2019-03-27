package com.tor.clientserver.communication;

import com.tor.clientserver.model.Client;
import com.tor.clientserver.model.ClientNeighbours;
import com.tor.clientserver.util.FileWritter;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientCommunication {
    private ClientNeighbours clientNeighbours;

    private Client client;

    public ResponseEntity<ClientNeighbours> sendPostData(String ip, String sendTo) throws MalformedURLException {
        client = new Client(ip);

        RestTemplate restTemplate = new RestTemplate();

        // Data attached to the request.
        HttpEntity<Client> requestBody = new HttpEntity<>(client);

        // Send data to url
        String url = "http://localhost:" + sendTo;

        // Send request with POST method.
        ResponseEntity<ClientNeighbours> result
                = restTemplate.postForEntity(url, requestBody, ClientNeighbours.class);

        System.out.println("Status code:" + result.getStatusCode());
        // Code = 200.
        if (result.getStatusCode() == HttpStatus.OK) {
            this.clientNeighbours = result.getBody();
        }

        return result;
    }

    public void startAskingClient(int version, int sendTo) throws IOException {
        System.out.println("Enter client handler port/ip: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String ip = br.readLine();

        System.out.println("ClientServer Address : " + ip);

        try {

            System.out.println("Connected");

            sendPostData(ip, String.valueOf(sendTo));

            ArrayList<String> ips = new ArrayList<>(Arrays.asList(clientNeighbours.getIps().split(",")));
            System.out.println(ips);
            Random random = new Random();
            String myport = ips.get(random.nextInt(ips.size()));

            while (true) {
                connectToNeighbour(version, myport, ip);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Socket read Error");
        }

        System.out.println("Closing connection");
    }


    public void connectToNeighbour(Integer version, String connectToPort, String myPort) {
        try {
            System.out.println("Connected with neighbors");
            RestTemplate restTemplate = new RestTemplate();

            // Code = 200.

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            Long id = System.nanoTime();
            System.out.println("Enter message: ");
            System.out.println(id);
            String line = br.readLine();
            ArrayList<String> ips = new ArrayList<>(Arrays.asList(clientNeighbours.getIps().split(",")));
            System.out.println(ips);

            if (line.length() > 5) {
                if (line.substring(0, 4).equals("http")) {
                    FileWritter.write(version, id, line);
                }
            }

            for (String port : ips) {
                URL url;
                if (port.length() < 6) {
                    url = new URL("http://localhost:" + port + "/download");
                } else {
                    url = new URL("http://" + port + ":1215/download");
                }


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Data", "message " + line);
                conn.setRequestProperty("ID", String.valueOf(id));
                conn.setDoOutput(true);

                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                for (int c; (c = in.read()) >= 0; )
                    System.out.print((char) c);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

