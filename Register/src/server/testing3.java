package server;

public class testing3 {

    public static void main(String[] args) {
        ClientAndServer clientAndServer = new ClientAndServer(7500);
        new Thread(clientAndServer).start();

    }
}
