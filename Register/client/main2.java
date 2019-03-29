

public class main2 {

    public static void main(String[] args) {
        ClientListner server = new ClientListner(8000);
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
