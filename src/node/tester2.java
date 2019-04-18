package node;

import util.PropertyReader;
import java.io.IOException;

public class tester2 {

    public static void main(String[] args) {
        PropertyReader propertyReader;
        try {
            propertyReader = new PropertyReader("7000config.properties");
            int port = propertyReader.getPort();
            new Thread(new NodeController( "http://localhost","http://localhost:1215", port)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
