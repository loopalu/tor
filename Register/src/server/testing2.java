package server;

import java.io.File;
import java.io.IOException;

public class testing2 {

    private static final Integer ADDRESS = 7000;

    public static void main(String[] args) {
        ClientAndServer clientAndServer = new ClientAndServer(ADDRESS);
        File yourFile = new File((ADDRESS.toString() + ".txt"));
        try {
            yourFile.createNewFile(); // if file already exists will do nothing
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(clientAndServer).start();

    }
}
