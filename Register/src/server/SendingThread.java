package server;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;


//new Thread(new SendingThread(in)).start();
public class SendingThread implements Runnable {

    private BufferedReader in;
    private String line;

    public SendingThread(BufferedReader input) {
        System.out.println(input==null);
        this.in = input;
    }

    public void run() {
        
        try {
            System.out.println(in == null);
            System.out.println(in.readLine() == null);
            this.line = in.readLine();
            System.out.println("Line "+line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HTTP-HEADER: " + line);
        assert line != null;
        if (line.contains("POST")) {
            try {
                sendBack(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (line.contains("GET")) {
            try {
                sendForward(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
//                if (getMyRequest()) {
//                    download(in);
//                } else {
//                    sendForward(in);
//                }
        }
    }

    public void sendForward(BufferedReader in) throws IOException {
        getMessagelines(in);
//        System.out.println("ASD");
//        URL url = new URL("http://localhost:8500");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-Type", "text/plain");
//        conn.setDoOutput(true);
//
//        Reader inasd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
    }

    public void sendBack(BufferedReader in) throws IOException {
        getMessagelines(in);
//        System.out.println("sendForward");
//        URL url = new URL("http://localhost:8000");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "text/plain");
//        conn.setDoOutput(true);
//
//        Reader inasdasd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
    }

    public void download(BufferedReader in) throws IOException {
        URL website = new URL("http://pm1.narvii.com/6311/3d4ff752b939276f48975c010a0e3de1ef116d99_00.jpg");
        ReadableByteChannel byteChannel = Channels.newChannel(website.openStream());
        FileOutputStream outputStream = new FileOutputStream("chika.jpg");
        outputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
    }


    private void getMessagelines(BufferedReader in) throws IOException {
        String line = "";

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
        System.out.println("done");
        System.out.println(postData);
    }

    public boolean getMyRequest() {
        double lazyness = Math.random();
        double chance = 0.5;
        return lazyness <= chance;
    }
}