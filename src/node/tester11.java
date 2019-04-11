package node;

public class tester11 {

    public static void main(String[] args) {
        new Thread(new Client( "http://25.67.181.195","http://25.67.181.195:1215", 8000)).start();
    }
}
