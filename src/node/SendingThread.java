package node;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.FileReader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;


public class SendingThread implements Runnable {

    private String port;
    private ArrayList<String> httpText;
    private String postData;
    private String getData;
    private String requestMethod;
    private String id;
    private Integer timeToLive;

    SendingThread(int port, ArrayList<String> httpText, String postData) {
        this.port = String.valueOf(port);
        this.httpText = httpText;
        this.postData = postData;
    }

    /**
     * Run SendingThread and check if Post or Get method
     */
    public void run() {
        for (String string : httpText) {
            if (string.contains("HTTP/1.1")) {
                requestMethod = string;
            }
        }
        if (requestMethod.contains("POST")) {
            try {
                sendBack();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestMethod.contains("GET")) {
            if (isNotLazy()) {
                try {
                    download();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    sendForward();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Forward to neighbours
     * @throws IOException
     */
    private void sendForward() throws IOException {
        System.out.println("GET - forward");
        for (String string : httpText) {
            if (string.contains("url")) {
                this.getData = string.substring(5);
            }
            if (string.contains("id")) {
                this.id = string.substring(4);
            }
            if (string.contains("timetolive")) {
                this.timeToLive = Integer.valueOf(string.substring(12));
            }
        }
        if (timeToLive != null) {
            if (timeToLive > 0) {
                for (String neighbor : Client.getNeighbours()) {
                    URL url = new URL(neighbor);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "text/plain");
                    conn.setRequestProperty("url", getData);
                    conn.setRequestProperty("id", id);
                    conn.setRequestProperty("timetolive", String.valueOf(timeToLive-1));
                    conn.setDoOutput(true);
                    Reader inasd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    inasd.close();
                }
            }
        }
    }

    /**
     * Send back Post method respond
     * @throws IOException
     */
    private void sendBack() throws IOException {
        boolean myRequest = false;
        System.out.println("POST - back");
        for (String string : httpText) {
            if (string.contains("id")) {
                this.id = string.substring(4);
            }
            if (string.contains("timetolive")) {
                this.timeToLive = Integer.valueOf(string.substring(12));
            }
        }
        //System.out.println(httpText);
        ObjectMapper mapper = new ObjectMapper();
        if (timeToLive != null) {
            if (timeToLive > 0) {
                ArrayList<String> myRequests = FileReader.read(Integer.valueOf(port));
                for (String request : myRequests) {
                    System.out.println(id + " " + request);
                    if (request.equals(id)) {
                        myRequest = true;
                        break;
                    }
                }
                if (myRequest) {
                    //System.out.println(postData);

                    JSONParser parser = new JSONParser();
                    JSONObject bodyParts = null;
                    if (postData != null) {
                        try {
                            bodyParts = (JSONObject) parser.parse(postData);
                        } catch (ParseException e) {
                        }
                    }
                    if (bodyParts != null) {
                        String fileType = (String) bodyParts.get("fileType");
                        String encodedString = (String) bodyParts.get("content");
                        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);

                        // create output file
                        FileOutputStream outputStream2 = new FileOutputStream(port + "."+fileType);
                        outputStream2.write(decodedBytes);
                        outputStream2.close();
                    }
                } else {
                    timeToLive -= 1;
                    for (String neighbor : Client.getNeighbours()) {
                        URL url = new URL(neighbor);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setRequestProperty("id", id);
                        conn.setRequestProperty("timetolive", String.valueOf(timeToLive));
                        conn.setDoOutput(true);
                        OutputStream outputStream = conn.getOutputStream();
                        outputStream.write(postData.getBytes());
                        outputStream.close();
                        Reader inasdasd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                        inasdasd.close();
                    }
                }
            }
        }
    }

    /**
     * Download link from neighbour
     * @throws IOException
     */
    private void download() throws IOException {
        System.out.println("GET - download");
        ObjectMapper mapper1 = new ObjectMapper();

        for (String string : httpText) {
            if (string.contains("url")) {
                this.getData = URLDecoder.decode(string.substring((5)));
            }
            if (string.contains("id")) {
                this.id = string.substring(4);
            }
        }

        boolean myRequest = false;
        ArrayList<String> myRequests = FileReader.read(Integer.valueOf(port));
        for (String request : myRequests) {
            if (request.equals(id)) {
                myRequest = true;
                break;
            }
        }
        if (!myRequest && getData != null) {
            String fileType;
            if (getData.contains(".jpg")) {
                fileType = "jpg";
            } else if (getData.contains(".pdf")) {
                fileType = "pdf";
            } else if (getData.contains(".gif")) {
                fileType = "gif";
            } else if (getData.contains(".txt")) {
                fileType = "txt";
            } else if (getData.contains(".html")) {
                fileType = "html";
            } else if (getData.contains(".rar")) {
                fileType = "rar";
            } else if (getData.contains(".zip")) {
                fileType = "zip";
            } else if (getData.contains(".bmp")) {
                fileType = "bmp";
            } else if (getData.contains(".jpeg")) {
                fileType = "jpeg";
            } else if (getData.contains(".png")) {
                fileType = "png";
            } else {
                fileType = "html";
            }
            URL website = new URL(getData);
            ReadableByteChannel byteChannel = Channels.newChannel(website.openStream());
            String fileName = port + "." + fileType;
            FileOutputStream outputStream = new FileOutputStream(fileName);
            outputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
            outputStream.close();

            File inputFile = new File(fileName);

            byte[] fileContent = FileUtils.readFileToByteArray(inputFile);
            String encodedString = Base64.getEncoder().encodeToString(fileContent);

            for (String neighbor : Client.getNeighbours()) {
                PostPackage postPackage = new PostPackage();
                postPackage.setStatus(200);
                postPackage.setMimetype("text/html");
                postPackage.setFileType(fileType);
                postPackage.setContent(encodedString.replaceAll("[\\x00-\\x09\\x11\\x12\\x14-\\x1F\\x7F\\x04]", ""));
                String outData = mapper1.writeValueAsString(postPackage);
                //System.out.println(outData);

                URL url = new URL(neighbor);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("timetolive", "20");
                conn.setRequestProperty("id", id);
                conn.setDoOutput(true);
                OutputStream outputStream2 = conn.getOutputStream();
                outputStream2.write(outData.getBytes());
                outputStream2.close();
                Reader inasdasd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                inasdasd.close();
            }
        }
    }

    private boolean isNotLazy() {
        double lazyness = Math.random();
        double chance = 0.5;
        return lazyness <= chance;
    }
}