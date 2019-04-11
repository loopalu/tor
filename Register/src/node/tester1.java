package node;

public class tester1 {

    public static void main(String[] args) {
        new Thread(new NodeController( "http://localhost","http://localhost:1215", 9000)).start();
    }
}
