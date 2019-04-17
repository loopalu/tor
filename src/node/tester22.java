package node;

public class tester22 {

    public static void main(String[] args) {
        new Thread(new NodeController( "http://192.168.1.158","http://192.168.1.158:1215", 9000)).start();
    }
}
