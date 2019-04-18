package node;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ConsoleReader implements Runnable {

    private int port;
    private Socket clientSocket;

    ConsoleReader(int port, Socket clientSocket) {
        this.port = port;
        this.clientSocket = clientSocket;
    }

    /**
     * Run ConsoleReader to receive URL from console
     */
    public void run() {
        try {
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            String ip = clientSocket.getRemoteSocketAddress().toString();
            ArrayList<String> httpText = new ArrayList<String>();

            httpText.add(ip);

            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line;
            line = in.readLine();
            httpText.add(line);

            // looks for post data
            int postDataI = -1;
            while ((line = in.readLine()) != null && (line.length() != 0)) {
                httpText.add(line);
                if (line.contains("Content-Length:")) {
                    postDataI = new Integer(line.substring(line.indexOf("Content-Length:") + 16, line.length()));
                }
            }
            String postData = null;

            // read the post data
            if (postDataI > 0) {
                char[] charArray = new char[postDataI];
                in.read(charArray, 0, postDataI);
                postData = new String(charArray);
            }
            new Thread(new RequestHandler(port, httpText, postData)).start();
            String message = "HTTP/1.1 200 OK";

            output.write(message.getBytes());
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
