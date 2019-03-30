import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ClientAndServer implements Runnable {

    public static int myIp;
    public static ArrayList<String> neigbors = new ArrayList<>();
    public String acting;
    public Boolean isRunning;
    public String prev = "";

    public ClientAndServer(int port) {
        myIp = port;
        this.isRunning = true;
    }

    public void run() {
        if (prev.equals("")) {
            
        }
        while (isRunning) {
            try {
                URL urlServer = new URL("http://localhost:9000");
                HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
                urlConn.connect();
                if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    acting = "Client";
                } else {
                    acting = "Server";
                }
            } catch (IOException e) {
                e.printStackTrace();
                acting = "Server";
            }
            if (!(prev.equals(acting))){
                if (acting.equals("Client")) {
                } else {

                }
            }
            this.prev = acting;
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    public static int getMyIp() {
        return myIp;
    }

    public void setNeigbors() {

    }

    public static ArrayList<String> getNeigbors(){
        return neigbors;
    }
}
