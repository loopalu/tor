package node;

public class tester22 {

    public static void main(String[] args) {
        new Thread(new Client( "http://25.67.181.195","http://25.67.181.195:1215", 9000)).start();
    }
}
