package node;

public class tester33 {

    public static void main(String[] args) {
        new Thread(new Client( "http://25.67.167.166","http://25.67.181.195:1215", 7000)).start();
    }
}
