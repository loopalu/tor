package node;

public class tester11 {

    public static void main(String[] args) {
        new Thread(new Client( "http://192.168.1.158","http://192.168.1.158:1215", 8000)).start();
    }
}
