import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class coder {

    public static void main(String[] args) {

        try {
            URL url = new URL("runescape.com");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Oige");
                //continue with client
            }
        } catch (IOException e) {
            System.out.println("Couldnt access");
            //Start server:
            //Register server = new Register(9000);
            //new Thread(server).start();
            //Code for closing server:
            //server.stop();

        }
    }
}