package node;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import util.FileReader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;


public class SendingThread implements Runnable {

    private String port;
    private ArrayList<String> httpText;
    private String postData;
    private double lazyness;
    private String getData;
    private String requestMethod;
    private String messageId;
    private Integer timeToLive;

    SendingThread(int port, ArrayList<String> httpText, String postData) {
        this.port = String.valueOf(port);
        this.httpText = httpText;
        this.postData = postData;
    }

    public void run() {
        //System.out.println(httpText);
        //System.out.println(postData);
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
            if (getMyRequest()) {
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

    private void sendForward() throws IOException {
        System.out.println("GET - forward");
        System.out.println("Lazyness: "+ lazyness);
        for (String string : httpText) {
            if (string.contains("veebiaadress")) {
                System.out.println(string);
                this.getData = string.substring(14);
            }
            if (string.contains("messageid")) {
                this.messageId = string.substring(11);
            }
            if (string.contains("timetolive")) {
                this.timeToLive = Integer.valueOf(string.substring(12));
            }
        }
        if (timeToLive != null) {
            if (timeToLive > 0) {
                //System.out.println(getData);
                //System.out.println(messageId);
                for (String neighbor : Client.getNeighbours()) {
                    URL url = new URL(neighbor);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "text/plain");
                    conn.setRequestProperty("veebiaadress", getData);
                    conn.setRequestProperty("messageid",messageId);
                    conn.setRequestProperty("timetolive", String.valueOf(timeToLive-1));
                    conn.setDoOutput(true);
                    Reader inasd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    inasd.close();
                }
            }
        }
    }

    private void sendBack() throws IOException {
        boolean myRequest = false;
        System.out.println("POST - back");
        System.out.println(httpText);
        ObjectMapper mapper = new ObjectMapper();
        //System.out.println(postData);
        //System.out.println(httpText);
        PostPackage postPackage = mapper.readValue(postData, PostPackage.class);
        String id = postPackage.getId();
        Integer timetolive = postPackage.getTimetolive();
        if (timetolive != null) {
            if (timetolive > 0) {
                ArrayList<String> myRequests = FileReader.read(Integer.valueOf(port));
                for (String request : myRequests) {
                    if (request.equals(id)) {
                        myRequest = true;
                        break;
                    }
                }
                if (myRequest) {
                    String fileType = postPackage.getFileType();
                    String encodedString = postPackage.getContent();
                    byte[] decodedBytes = Base64.getDecoder().decode(encodedString);

                    // create output file
                    FileOutputStream outputStream2 = new FileOutputStream(port + "."+fileType);
                    outputStream2.write(decodedBytes);
                    outputStream2.close();
                } else {
                    timeToLive = postPackage.getTimetolive() - 1;
                    postPackage.setTimetolive(timeToLive);
                    String forward = mapper.writeValueAsString(postPackage);
                    for (String neighbor : Client.getNeighbours()) {
                        URL url = new URL(neighbor);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setDoOutput(true);
                        OutputStream outputStream = conn.getOutputStream();
                        outputStream.write(forward.getBytes());
                        outputStream.close();
                        Reader inasdasd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                        inasdasd.close();
                    }
                }
            }
        }
    }

    private void download() throws IOException {
        System.out.println("GET - download");
        System.out.println("Lazyness: "+ lazyness);
        ObjectMapper mapper1 = new ObjectMapper();
        for (String string : httpText) {
            if (string.contains("veebiaadress")) {
                this.getData = URLDecoder.decode(string.substring((14)));
            }
            if (string.contains("messageid")) {
                this.messageId = string.substring(11);
            }
        }

        boolean myRequest = false;
        ArrayList<String> myRequests = FileReader.read(Integer.valueOf(port));
        for (String request : myRequests) {
            if (request.equals(messageId)) {
                myRequest = true;
                break;
            }
        }
        if (!myRequest) {
            //System.out.println(getData);
            //System.out.println(messageId);
            String fileType;
            if (getData.contains(".jpg")) {
                fileType = "jpg";
            } else if (getData.contains(".mp3")) {
                fileType = "mp3";
            } else if (getData.contains(".pdf")) {
                fileType = "pdf";
            } else if (getData.contains(".gif")) {
                fileType = "gif";
            } else if (getData.contains(".txt")) {
                fileType = "txt";
            } else if (getData.contains(".htm")) {
                fileType = "htm";
            } else if (getData.contains(".html")) {
                fileType = "html";
            } else if (getData.contains(".doc")) {
                fileType = "doc";
            } else if (getData.contains(".rtf")) {
                fileType = "rtf";
            } else if (getData.contains(".wav")) {
                fileType = "wav";
            } else if (getData.contains(".7z")) {
                fileType = "7z";
            } else if (getData.contains(".tar.gz")) {
                fileType = "tar.gz";
            } else if (getData.contains(".deb")) {
                fileType = "deb";
            } else if (getData.contains(".rar")) {
                fileType = "rar";
            } else if (getData.contains(".zip")) {
                fileType = "zip";
            } else if (getData.contains(".bin")) {
                fileType = "bin";
            } else if (getData.contains(".iso")) {
                fileType = "iso";
            } else if (getData.contains(".csv")) {
                fileType = "csv";
            } else if (getData.contains(".dat")) {
                fileType = "dat";
            } else if (getData.contains(".db")) {
                fileType = "db";
            } else if (getData.contains(".log")) {
                fileType = "log";
            } else if (getData.contains(".torrent")) {
                fileType = "torrent";
            } else if (getData.contains(".mdb")) {
                fileType = "mdb";
            } else if (getData.contains(".sql")) {
                fileType = "sql";
            } else if (getData.contains(".tar")) {
                fileType = "tar";
            } else if (getData.contains(".xml")) {
                fileType = "xml";
            } else if (getData.contains(".js")) {
                fileType = "js";
            } else if (getData.contains(".jar")) {
                fileType = "jar";
            } else if (getData.contains(".apk")) {
                fileType = "apk";
            } else if (getData.contains(".bat")) {
                fileType = "bat";
            } else if (getData.contains(".bin")) {
                fileType = "bin";
            } else if (getData.contains(".cgi")) {
                fileType = "cgi";
            } else if (getData.contains(".pl")) {
                fileType = "pl";
            } else if (getData.contains(".exe")) {
                fileType = "exe";
            } else if (getData.contains(".py")) {
                fileType = "py";
            } else if (getData.contains(".ttf")) {
                fileType = "ttf";
            } else if (getData.contains(".bmp")) {
                fileType = "bmp";
            } else if (getData.contains(".ico")) {
                fileType = "ico";
            } else if (getData.contains(".jpeg")) {
                fileType = "jpeg";
            } else if (getData.contains(".jpg")) {
                fileType = "jpg";
            } else if (getData.contains(".png")) {
                fileType = "png";
            } else if (getData.contains(".asp")) {
                fileType = "asp";
            } else if (getData.contains(".aspx")) {
                fileType = "aspx";
            } else if (getData.contains(".cer")) {
                fileType = "cer";
            } else if (getData.contains(".css")) {
                fileType = "css";
            } else if (getData.contains(".jsp")) {
                fileType = "jsp";
            } else if (getData.contains(".php")) {
                fileType = "php";
            } else if (getData.contains(".rss")) {
                fileType = "rss";
            } else if (getData.contains(".xhtml")) {
                fileType = "xhtml";
            } else if (getData.contains(".key")) {
                fileType = "key";
            } else if (getData.contains(".pps")) {
                fileType = "pps";
            } else if (getData.contains(".ppt")) {
                fileType = "ppt";
            } else if (getData.contains(".class")) {
                fileType = "class";
            } else if (getData.contains(".java")) {
                fileType = "java";
            } else if (getData.contains(".cpp")) {
                fileType = "cpp";
            } else if (getData.contains(".vb")) {
                fileType = "vb";
            } else if (getData.contains(".xls")) {
                fileType = "xls";
            } else if (getData.contains(".bak")) {
                fileType = "bak";
            } else if (getData.contains(".dll")) {
                fileType = "dll";
            } else if (getData.contains(".cfg")) {
                fileType = "cfg";
            } else if (getData.contains(".dmp")) {
                fileType = "dmp";
            } else if (getData.contains(".ini")) {
                fileType = "ini";
            } else if (getData.contains(".msi")) {
                fileType = "msi";
            } else if (getData.contains(".sys")) {
                fileType = "sys";
            } else if (getData.contains(".avi")) {
                fileType = "avi";
            } else if (getData.contains(".flv")) {
                fileType = "flv";
            } else if (getData.contains(".mp4")) {
                fileType = "mp4";
            } else if (getData.contains(".mpg")) {
                fileType = "mpg";
            } else if (getData.contains(".mpeg")) {
                fileType = "mpeg";
            } else if (getData.contains(".wmv")) {
                fileType = "wmv";
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
                postPackage.setId(messageId);
                postPackage.setTimetolive(20);
                postPackage.setFileType(fileType);
                postPackage.setContent(encodedString);
                String outData = mapper1.writeValueAsString(postPackage);
                System.out.println(outData);

                URL url = new URL(neighbor);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                OutputStream outputStream2 = conn.getOutputStream();
                outputStream2.write(outData.getBytes());
                outputStream2.close();
                Reader inasdasd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                inasdasd.close();
            }
        }
    }

    private boolean getMyRequest() {
        lazyness = Math.random();
        double chance = 0.5;
        return lazyness <= chance;
    }
}