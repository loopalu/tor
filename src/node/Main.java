package node;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Main {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://i1.wp.com/knuckledraggin.com/wp-content/uploads/2018/07/z.gif");
        HttpURLConnection connection = (HttpURLConnection)  url.openConnection();
        connection.setRequestMethod("HEAD");
        connection.connect();
        String contentType = connection.getContentType();
        System.out.println(contentType);
        connection.disconnect();
    }
}
