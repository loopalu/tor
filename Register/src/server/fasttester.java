package server;

public class fasttester {

    public static void main(String[] args) {
        new Thread(new Client( "http://localhost:9000","http://localhost:1215")).start();
    }
}
