import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

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
                if (getMyRequest()) {
                    download(in);
                } else {
                    sendForward(in);
                }
            }

            output.close();
            input.close();
            clientSocket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendForward(BufferedReader in) throws IOException {
        System.out.println("sendForward");
        getMessagelines(in);


    }

    public void sendBack(BufferedReader in) throws IOException {
        System.out.println("sendBack");
        getMessagelines(in);
    }

    public void download(BufferedReader in) throws IOException {
        System.out.println("download");
        getMessagelines(in);
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
        System.out.println(postData);
    }

    public boolean getMyRequest() {
        double lazyness = Math.random();
        double chance = 0.5;
        return lazyness <= chance;
    }
}
