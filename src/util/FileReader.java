package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    /**
     * Read file
     * @param port port
     * @return array of string
     * @throws FileNotFoundException
     */
    public static ArrayList<String> read(Integer port) throws FileNotFoundException {
        ArrayList<String> lines = new ArrayList<>();
        Scanner scanner = null;
        scanner = new Scanner(new File((port.toString() + ".txt")));
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        scanner.close();
        return lines;
    }
}