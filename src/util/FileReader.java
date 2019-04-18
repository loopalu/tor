package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    /**
     * Read file
     *
     * @param port Address/port of the node
     * @return List of requests' ids
     * @throws FileNotFoundException The exeption when it is not possible to read the file
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