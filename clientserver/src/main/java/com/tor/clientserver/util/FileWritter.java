package com.tor.clientserver.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileWritter {
    public static void write(int version, Long id, String text) throws IOException {
        BufferedWriter writer = null;
        switch (version) {
            case 1:
                writer = new BufferedWriter(new FileWriter("id1.txt"));
                break;
            case 2:
                writer = new BufferedWriter(new FileWriter("id2.txt"));
                break;
            case 3:
                writer = new BufferedWriter(new FileWriter("id3.txt"));
                break;
        }
        if (writer != null) {
            writer.write(id.toString() + ":" + text);
            writer.newLine();
            writer.close();
        }
    }
}
