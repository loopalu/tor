package com.tor.clientserver.clientServer;

import com.tor.clientserver.model.Client;
import com.tor.clientserver.model.ClientNeighbours;
import com.tor.clientserver.model.Connect;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.*;
import java.util.*;

public class CreateClient {

    static final String URL_CREATE_CLIENT = "http://localhost:9000/";
    private ClientNeighbours clientNeighbours;

    private static String host = "localhost";
    private static int port = 9000;

    private Socket socket;

    public ResponseEntity<ClientNeighbours> createClient(String ip, String action) throws MalformedURLException {

        Client client = new Client(ip, action);

        RestTemplate restTemplate = new RestTemplate();

        // Data attached to the request.
        HttpEntity<Client> requestBody = new HttpEntity<>(client);

        // Send request with POST method.
        ResponseEntity<ClientNeighbours> result
                = restTemplate.postForEntity(URL_CREATE_CLIENT, requestBody, ClientNeighbours.class);

        System.out.println("Status code:" + result.getStatusCode());
        // Code = 200.
        if (result.getStatusCode() == HttpStatus.OK) {
            this.clientNeighbours = result.getBody();
        }

        return result;
    }

    public void startAskingClient(String ip) throws IOException {
        String line = "";
        String neighbours;
        BufferedReader br;

        System.out.println("Client Address : " + ip);


        try (Socket socket = new Socket(host, port)) {

            System.out.println("Connected");

            br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Enter action(Enter or Leave): ");
            line = br.readLine();


            createClient(ip, line);


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!line.equals("Leave")) {
            try {

                br = new BufferedReader(new InputStreamReader(System.in));


                System.out.println("Enter neighbour to connect with: " + clientNeighbours.getIps());
                line = br.readLine();

                if (!line.equals("Leave")) {
                    ArrayList<String> ips = new ArrayList<>(Arrays.asList(clientNeighbours.getIps().split(",")));
                    Random random = new Random();
                    String myport = ips.get(random.nextInt(ips.size()));
                    connectToNeighbour(myport, ip);
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Socket read Error");
            }
        }

        System.out.println("Closing connection");

        // close connection
        socket.close();

    }


    public void connectToNeighbour(String connectToPort, String myPort) {
        try (Socket socket = new Socket(host, Integer.parseInt(connectToPort))) {
            System.out.println("Connected with neighbors");
            RestTemplate restTemplate = new RestTemplate();

            Connect connect = new Connect();
            connect.setLetsConnect(true);
            connect.setPort(myPort);

            // Data attached to the request.
            HttpEntity<Connect> requestBody = new HttpEntity<>(connect);

            // Send request with POST method.
            ResponseEntity<Connect> result
                    = restTemplate.postForEntity("http://localhost:" + connectToPort, requestBody, Connect.class);

            // Code = 200.
            if (result.getStatusCode() == HttpStatus.OK) {
                Connect connect2 = result.getBody();

                if (connect2 != null) {
                    System.out.println("Connected with: http://localhost:" + connectToPort + " " + connect2.getLetsConnect());
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                    System.out.println("Enter message: ");
                    String line = br.readLine();
                    ArrayList<String> ips = new ArrayList<>(Arrays.asList(clientNeighbours.getIps().split(",")));
                    long id = System.nanoTime();

                    for (String port: ips) {
                        URL url = new URL("http://localhost:" + port + "/download");


                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Data", line);
                        conn.setRequestProperty("ID", String.valueOf(id));
                        conn.setDoOutput(true);

                        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                        for (int c; (c = in.read()) >= 0;)
                            System.out.print((char)c);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

