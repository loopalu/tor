package node;

import util.PropertyReader;
import java.io.IOException;

public class tester2 {

    public static void main(String[] args) {
        PropertyReader propertyReader;
        try {
            propertyReader = new PropertyReader("7000config.properties");
            int port = propertyReader.getPort();
            String registryIP = propertyReader.getRegistryIP();
            String myIP = propertyReader.getMyIP();
            new Thread(new NodeController( myIP,registryIP, port)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
