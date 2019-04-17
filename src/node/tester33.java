package node;

public class tester33 {

    public static void main(String[] args) {
        new Thread(new NodeController( "http://192.168.1.193","http://192.168.1.158:1215", 7000)).start();
    }
}
