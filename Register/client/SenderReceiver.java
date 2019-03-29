import java.io.*;
import java.net.Socket;

public class SenderReceiver implements Runnable {

    protected Socket clientSocket;


    public SenderReceiver(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
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
            String message = "HTTP/1.1 200 OK";
            output.write(message.getBytes());
            output.close();
            input.close();
            clientSocket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendForward(BufferedReader in) {

    }

    public void sendBack(BufferedReader in) {

    }

    public void download(BufferedReader in) {

    }

    public boolean getMyRequest() {
        double rand = Math.random();
        double chance = 0.5;
        return rand <= chance;
    }
}
