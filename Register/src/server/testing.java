package server;

public class testing {

    public static void main(String[] args) {
        ClientAndServer clientAndServer = new ClientAndServer(8000);
        new Thread(clientAndServer).start();

    }
}
