package node;

public class tester22 {

    public static void main(String[] args) {
        new Thread(new Client( "http://192.168.1.70","http://192.168.1.70:1215", 9000)).start();
    }
}
