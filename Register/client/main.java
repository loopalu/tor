

public class main {

    public static void main(String[] args) {
        ClientListner server = new ClientListner(9000);
        new Thread(server).start();

        try {
            Thread.sleep(20 * 1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping Server");
        server.stop();
    }
}