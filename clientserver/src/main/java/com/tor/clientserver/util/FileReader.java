package com.tor.clientserver.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    public static ArrayList<String> read(int id) throws FileNotFoundException {
        ArrayList<String> lines = new ArrayList<>();
        Scanner scanner = null;
        switch (id) {
            case 1:
                scanner = new Scanner(new File("id1.txt"));
                break;
            case 2:
                scanner = new Scanner(new File("id2.txt"));
                break;
            case 3:
                scanner = new Scanner(new File("id3.txt"));
                break;
        }
        assert scanner != null;
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        scanner.close();
        return lines;
    }

}