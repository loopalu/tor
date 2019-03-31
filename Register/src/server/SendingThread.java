package server;

import java.io.BufferedReader;


//new Thread(new SendingThread(in)).start();
public class SendingThread implements Runnable {

    private BufferedReader in;

    public SendingThread(BufferedReader input) {
        this.in = input;
    }

    public void run() {

    }
}
