package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    /**
     * Read file
     *
     * @param file Name of the file
     * @return List of lines from file
     * @throws FileNotFoundException The exeption when it is not possible to read the file
     */
    public static ArrayList<String> read(String file) throws FileNotFoundException {
        ArrayList<String> lines = new ArrayList<>();
        Scanner scanner = null;
        scanner = new Scanner(new File((file)));
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        scanner.close();
        return lines;
    }
}