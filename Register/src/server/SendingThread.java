package server;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;


//new Thread(new SendingThread(in)).start();
public class SendingThread implements Runnable {

    private ArrayList<String> neighbors = new ArrayList<>(Arrays.asList("7500", "8000", "8500"));
    private ArrayList<String> httpText;
    private String postData;
    private double lazyness;
    private String getData;

    public SendingThread(ArrayList<String> httpText, String postData) {
        this.httpText = httpText;
        this.postData = postData;
    }

    public void run() {
        //System.out.println(httpText);
        //System.out.println(postData);
        String requestMethod = httpText.get(1);
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

    public void sendForward() throws IOException {
        System.out.println(neighbors);
        System.out.println("GET - forward");
        System.out.println("Lazyness: "+ lazyness);
        for (String string : httpText) {
            if (string.contains("veebiaadress")) {
                this.getData = string;
            }
        }
        System.out.println(getData);
//        URL url = new URL("http://localhost:8500");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-Type", "text/plain");
//        conn.setDoOutput(true);
//
//        Reader inasd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
    }

    public void sendBack() throws IOException {
        System.out.println("POST - back");
//        URL url = new URL("http://localhost:8000");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "text/plain");
//        conn.setDoOutput(true);
//
//        Reader inasdasd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
    }

    public void download() throws IOException {
        System.out.println("GET - download");
        System.out.println("Lazyness: "+ lazyness);
//        URL website = new URL("http://pm1.narvii.com/6311/3d4ff752b939276f48975c010a0e3de1ef116d99_00.jpg");
//        ReadableByteChannel byteChannel = Channels.newChannel(website.openStream());
//        FileOutputStream outputStream = new FileOutputStream("chika.jpg");
//        outputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
    }

    public boolean getMyRequest() {
        lazyness = Math.random();
        double chance = 0.5;
        return lazyness <= chance;
    }
}