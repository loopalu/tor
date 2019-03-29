import java.util.HashMap;
import java.util.Scanner;

public class Client implements Runnable{

    public boolean isRunning = false;
    public HashMap requests = new HashMap();

    public Client() {
        isRunning = true;
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



}
