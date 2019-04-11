package node;

public class tester2 {

    public static void main(String[] args) {
        new Thread(new Client( "http://localhost","http://localhost:1215", 7000)).start();
    }
}
