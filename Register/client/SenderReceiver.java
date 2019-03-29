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
        System.out.println("sendForward");
        ArrayList<String> lines = getMessagelines(in);
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(lines);

    }

    public void sendBack(BufferedReader in) {
        System.out.println("sendBack");
        ArrayList<String> lines = getMessagelines(in);
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(lines);
    }

    public void download(BufferedReader in) {
        System.out.println("download");
        ArrayList<String> lines = getMessagelines(in);
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(lines);
    }

    
    private ArrayList<String> getMessagelines(BufferedReader in) {
        System.out.println("getMessageLines");
        ArrayList<String> messagageLines = new ArrayList<>();
        String inputLine;
        while (true) {
            try {
                inputLine = in.readLine();
                System.out.println(inputLine);
                if (inputLine == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }messagageLines.add(inputLine);
        System.out.println("done");
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messagageLines;
    }

    public boolean getMyRequest() {
        double lazyness = Math.random();
        double chance = 0.5;
        return lazyness <= chance;
    }
}
