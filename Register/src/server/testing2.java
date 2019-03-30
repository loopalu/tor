package server;

public class testing2 {

    public static void main(String[] args) {
        ClientAndServer clientAndServer = new ClientAndServer(7000);
        new Thread(clientAndServer).start();

    }
}
