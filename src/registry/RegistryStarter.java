package registry;

public class RegistryStarter {

    public static void main(String[] args) {
        Registry server = new Registry(1215);
        new Thread(server).start();

        try {
            Thread.sleep(6000 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping Server");
        server.stop();
    }
}
