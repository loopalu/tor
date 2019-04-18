package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileWritter {

    /**
     * Write file
     *
     * @param port Address/port of the node
     * @param id Request id
     * @param bool Boolean to show if request has been received or not
     * @throws IOException The exception when it is not possible to write into file
     */
    public static void write(String port, String id, boolean bool) throws IOException {
        BufferedWriter writer = null;
        writer = new BufferedWriter(new FileWriter((port + ".txt")));
        writer.write(id + ":" + bool);
        writer.newLine();
        writer.close();
    }
}
