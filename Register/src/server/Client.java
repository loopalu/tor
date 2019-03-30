package server;

import java.util.ArrayList;
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
            String urlString = reader.nextLine();
            if (ClientAndServer.getNeighbours().size() >= 2){
                for (String i : ClientAndServer.getNeighbours()) {
                    String url = "http://localhost:" + ClientAndServer.getMyIp();
                    System.out.println(i);
                }
            } else {
                System.out.println("Pole piisavalt naabreid!");
            }
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
