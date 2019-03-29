import java.util.HashMap;
import java.util.Scanner;

public class Client implements Runnable{

    public boolean isRunning;

    public Client() {
        this.isRunning = true;
    }

    public void run() {

        while (isRunning) {
            Scanner reader = new Scanner(System.in);
            System.out.println("Enter page: ");
            String url = reader.nextLine();


        }
    }
    public synchronized void stop() {
        this.isRunning = false;
    }

    public synchronized void suspend() {
        this.isRunning = false;
    }

    public synchronized void resume() {
        this.isRunning = true;
    }


}
