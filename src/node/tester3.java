package node;

public class tester3 {

    public static void main(String[] args) {
        new Thread(new Client( "http://localhost","http://localhost:1215", 8000)).start();
    }
}
