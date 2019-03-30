package server;

import java.util.ArrayList;
import java.util.Scanner;

public class Client implements Runnable{

    public boolean isRunning;
    public ArrayList<String> neighbors = new ArrayList<>();

    public Client() {
        this.isRunning = true;
    }

    public void run() {

        while (isRunning) {
            Scanner reader = new Scanner(System.in);
            System.out.println("Enter page: ");
            String urlString = reader.nextLine();
            if (neighbors.size() >= 2){
                for (String i : ClientAndServer.getNeigbors()) {
                    String url = "http://localhost:" + ClientAndServer.getMyIp();
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
