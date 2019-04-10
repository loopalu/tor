package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileWritter {
    public static void write(String port, String id) throws IOException {
        BufferedWriter writer = null;
        writer = new BufferedWriter(new FileWriter((port.toString() + ".txt")));
        writer.write(id);
        writer.newLine();
        writer.close();
    }
}
