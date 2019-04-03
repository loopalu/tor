package server;

import java.io.File;
import java.io.IOException;

public class testing4 {

    private static final Integer ADDRESS = 8500;

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
