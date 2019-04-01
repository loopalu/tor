package server;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;


//new Thread(new SendingThread(in)).start();
public class SendingThread implements Runnable {

    private ArrayList<String> httpText;
    private String postData;

    public SendingThread(ArrayList<String> httpText, String postData) {
    }

    public void run() {
//        if (line.contains("POST")) {
//            try {
//                sendBack(in);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else if (line.contains("GET")) {
//            try {
//                sendForward(in);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//                if (getMyRequest()) {
//                    download(in);
//                } else {
//                    sendForward(in);
//                }
//        }
    }

    public void sendForward(BufferedReader in) throws IOException {
//        System.out.println("ASD");
//        URL url = new URL("http://localhost:8500");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-Type", "text/plain");
//        conn.setDoOutput(true);
//
//        Reader inasd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
    }

    public void sendBack() throws IOException {
//        System.out.println("sendForward");
//        URL url = new URL("http://localhost:8000");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "text/plain");
//        conn.setDoOutput(true);
//
//        Reader inasdasd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
    }

    public void download() throws IOException {
        URL website = new URL("http://pm1.narvii.com/6311/3d4ff752b939276f48975c010a0e3de1ef116d99_00.jpg");
        ReadableByteChannel byteChannel = Channels.newChannel(website.openStream());
        FileOutputStream outputStream = new FileOutputStream("chika.jpg");
        outputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
    }

    public boolean getMyRequest() {
        double lazyness = Math.random();
        double chance = 0.5;
        return lazyness <= chance;
    }
}