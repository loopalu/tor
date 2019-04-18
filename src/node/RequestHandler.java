package node;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.FileReader;

import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;


public class RequestHandler implements Runnable {

    private String port;
    private ArrayList<String> httpText;
    private String postData;
    private String getData;
    private String requestMethod;
    private String id;
    private Integer timeToLive;

    /**
     * Initializes the RequestHandler with given information
     *
     * @param port The address/port of current node
     * @param httpText The headers
     * @param postData Body of the request
     */
    RequestHandler(int port, ArrayList<String> httpText, String postData) {
        this.port = String.valueOf(port);
        this.httpText = httpText;
        this.postData = postData;
    }

    /**
     * Runs RequestHandler and check if method is method is POST or GET
     */
    public void run() {
        // Gets request method (GET or POST) out from headers
        for (String string : httpText) {
            if (string.contains("HTTP/1.1")) {
                requestMethod = string;
            }
        }
        // If received request is POST request then node forwards request back to neighbors
        if (requestMethod.contains("POST")) {
            try {
                sendBack();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestMethod.contains("GET")) {
            // Checks the laziness of node. If not lazy, then tries to download file
            if (isNotLazy()) {
                try {
                    download();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            // If node is lazy, then forwards GET request to neighbors
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
     * Forwards GET request to neighbours
     *
     * @throws IOException The exception in the case of network error
     */
    private void sendForward() throws IOException {
        System.out.println("GET - forward");
        // Get url, id and timetolive out from headers
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
            // If timetolive is not 0, then forwards the request
            if (timeToLive > 0) {
                for (String neighbor : NodeController.getNeighbours()) {
                    URL url = new URL(neighbor);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "text/plain");
                    conn.setRequestProperty("url", getData);
                    conn.setRequestProperty("id", id);
                    conn.setRequestProperty("timetolive", String.valueOf(timeToLive - 1));
                    conn.setDoOutput(true);

                    Reader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    reader.close();
                }
            }
        }
    }

    /**
     * Sends POST request back with downloaded file
     *
     * @throws IOException The exception in the case it is not possible to write into the file
     */
    private void sendBack() throws IOException {
        boolean myRequest = false;
        System.out.println("POST - back");

        // Gets id and timetolive out from headers
        for (String string : httpText) {
            if (string.contains("id")) {
                this.id = string.substring(4);
            }
            if (string.contains("timetolive")) {
                this.timeToLive = Integer.valueOf(string.substring(12));
            }
        }
        if (timeToLive != null) {
            // If timetolive is not 0 then tries to process the request (either download or forward to neighbors)
            if (timeToLive > 0) {
                // Reads the ids of requests that current node has sent out
                ArrayList<String> myRequests = FileReader.read(Integer.valueOf(port));

                for (String request : myRequests) {
                    //System.out.println(id + " " + request); //For debug to see id-s of my own request and compare to id of received request

                    // Checks if node had sent out request with given id
                    if (request.equals(id)) {
                        myRequest = true;
                        break;
                    }
                }
                // If current node sent out request with given id then tries to save file that was sent back
                if (myRequest) {
                    JSONParser parser = new JSONParser();
                    JSONObject bodyParts = null;
                    if (postData != null) {
                        try {
                            bodyParts = (JSONObject) parser.parse(postData);
                        } catch (ParseException e) {
                        }
                    }
                    if (bodyParts != null) {
                        String status = (String) bodyParts.get("status");

                        // If there was some problem with request then show the error message
                        if (status.equals("404")) {
                            String errorMessage = (String) bodyParts.get("message");
                            System.out.println(errorMessage);

                        // If nothing wrong with request then saves file
                        } else {
                            String fileType = (String) bodyParts.get("fileType");
                            String encodedString = (String) bodyParts.get("content");
                            byte[] decodedBytes = Base64.getDecoder().decode(encodedString);

                            // create output file
                            FileOutputStream outputStream = new FileOutputStream(port + "." + fileType);
                            outputStream.write(decodedBytes);
                            outputStream.close();
                        }
                    }
                // If it is not my request then forward it to neighbors
                } else {
                    timeToLive -= 1;
                    for (String neighbor : NodeController.getNeighbours()) {
                        URL url = new URL(neighbor);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setRequestProperty("id", id);
                        connection.setRequestProperty("timetolive", String.valueOf(timeToLive));
                        connection.setDoOutput(true);

                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(postData.getBytes());
                        outputStream.close();
                        Reader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                        reader.close();
                    }
                }
            }
        }
    }

    /**
     *  Tries to download requested file
     *
     * @throws IOException The exception in the case file does not exist
     */
    private void download() throws IOException {
        System.out.println("GET - download");

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

            // Validates the URL and acts accordingly
            String urlValidity = urlValidator(getData);
            if (urlValidity.equals("Host does not exist")) {
                sendError("Host does not exist!");
            } else if (urlValidity.equals("false")) {
                sendError("Host is not available!");
            } else {
                downloadAndSend(getData);
            }
        }
    }

    /**
     * Download requested file and send it out with POST request
     *
     * @throws IOException The exeption in case file is not found
     */
    private void downloadAndSend(String getData) throws IOException {
        String fileType = fileType(getData);

        URL website = new URL(getData);
        ReadableByteChannel byteChannel = Channels.newChannel(website.openStream());

        // Save the url into appropriate file with relevant extension
        String fileName = port + "." + fileType;
        FileOutputStream outputStream = new FileOutputStream(fileName);

        outputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
        outputStream.close();

        File inputFile = new File(fileName);

        // Read downloaded file in and encode it with Base64
        byte[] fileContent = FileUtils.readFileToByteArray(inputFile);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        for (String neighbor : NodeController.getNeighbours()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", "200");
            jsonObject.put("mimetype", "text/html");

            // Remove all nonprintable characters from Base64 string (End Of File and others)
            jsonObject.put("content", encodedString.replaceAll("[\\x00-\\x09\\x11\\x12\\x14-\\x1F\\x7F\\x04]", ""));
            jsonObject.put("fileType", fileType);

            URL url = new URL(neighbor);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("timetolive", "20");
            connection.setRequestProperty("id", id);
            connection.setDoOutput(true);

            OutputStream outputStream2 = connection.getOutputStream();
            outputStream2.write(jsonObject.toString().getBytes());
            outputStream2.close();

            Reader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            reader.close();
        }
    }

    /**
     * Sends error with POST request when not possible to download the file
     *
     * @param string Errormessage to be sent
     * @throws IOException The exeption in case of network error
     */
    private void sendError(String string) throws IOException {
        for (String neighbor : NodeController.getNeighbours()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", "404");
            jsonObject.put("message", string);

            URL url = new URL(neighbor);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("timetolive", "20");
            connection.setRequestProperty("id", id);
            connection.setDoOutput(true);

            OutputStream outputStream2 = connection.getOutputStream();
            outputStream2.write(jsonObject.toString().getBytes());
            outputStream2.close();

            Reader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            reader.close();
        }
    }

    /**
     * Validates the url and returns outcome if url exists/is available
     *
     * @param getData Given URL
     * @return Url validity in the form of string
     * @throws IOException The exception for unknwn host
     */
    private String urlValidator(String getData) throws IOException {
        String hostname = new URL(getData).getHost();
        try {
            // Check if URL is reachable
            InetAddress address = InetAddress.getByName(hostname);
            return String.valueOf((address.isReachable(1000)));
        } catch (UnknownHostException e) {
            return "Host does not exist";
        }
    }

    /**
     * Checks if node is lazy or not, for downloading
     *
     * @return Answer if node is lazy
     */
    private boolean isNotLazy() {
        double lazyness = Math.random();
        double chance = 0.5;
        return lazyness <= chance;
    }

    /**
     * Checks the URL for file type
     *
     * @return  Found file type
     */
    private String fileType(String getData) {
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
        } else if (getData.contains(".bmp")) {
            fileType = "bmp";
        } else if (getData.contains(".jpeg")) {
            fileType = "jpeg";
        } else if (getData.contains(".png")) {
            fileType = "png";
        } else {
            fileType = "html";
        }
        return fileType;
    }
}