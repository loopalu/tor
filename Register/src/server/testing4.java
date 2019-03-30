package server;

public class testing4 {

    public static void main(String[] args) {
        ClientAndServer clientAndServer = new ClientAndServer(8500);
        new Thread(clientAndServer).start();

    }
}
