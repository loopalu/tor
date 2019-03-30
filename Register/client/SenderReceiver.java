import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

public class SenderReceiver implements Runnable {
    private ArrayList<Long> messages = new ArrayList<>();

    protected Socket clientSocket;


    public SenderReceiver(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String message = "HTTP/1.1 200 OK";
            output.write(message.getBytes());
            String line;
            line = in.readLine();
            System.out.println("HTTP-HEADER: " + line);
            if (line.contains("POST")) {
                sendBack(in);
            } else if (line.contains("GET")) {
                sendForward(in);
//                if (getMyRequest()) {
//                    download(in);
//                } else {
//                    sendForward(in);
//                }
            }

            output.close();
            input.close();
            clientSocket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendForward(BufferedReader in) throws IOException {
        long id = System.nanoTime();
        System.out.println("sendForward");
        URL url = new URL("http://localhost:8000");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Data", "message ");
        conn.setRequestProperty("ID", String.valueOf(id));
        conn.setDoOutput(true);
    }

    public void sendBack(BufferedReader in) throws IOException {
        long id = System.nanoTime();
        System.out.println("sendForward");
        URL url = new URL("http://localhost:8000");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Data", "message ");
        conn.setRequestProperty("ID", String.valueOf(id));
        conn.setDoOutput(true);
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
