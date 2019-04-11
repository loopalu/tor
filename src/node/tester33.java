package node;

public class tester33 {

    public static void main(String[] args) {
        new Thread(new Client( "http://192.168.1.115","http://192.168.1.70:1215", 7000)).start();
    }
}
