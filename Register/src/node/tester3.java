package node;

import node.Client;

public class tester3 {

    public static void main(String[] args) {
        new Thread(new Client( "http://localhost:8000","http://localhost:1215", 8000)).start();
    }
}
