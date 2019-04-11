package node;

public class tester1 {

    public static void main(String[] args) {
        new Thread(new Client( "http://localhost","http://localhost:1215", 9000)).start();
    }
}
