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
                sendBack();
            } else if (line.contains("GET")) {
                if (getMyRequest()) {
                    download();
                } else {
                    sendForward();
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

    public void sendForward() {

    }

    public void sendBack() {

    }

    public void download() {

    }

    public boolean getMyRequest() {
        double rand = Math.random();
        double chance = 0.5;
        return rand <= chance;
    }
}
