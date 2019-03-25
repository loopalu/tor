import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class Main {
    private static String message;
    public static void main(String[] args) throws IOException {
        URL website = new URL("http://pm1.narvii.com/6311/3d4ff752b939276f48975c010a0e3de1ef116d99_00.jpg");
        ReadableByteChannel byteChannel = Channels.newChannel(website.openStream());
        FileOutputStream outputStream = new FileOutputStream("chika.jpg");
        outputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
        System.out.println(encode("chika.jpg"));
        decode("base64.txt","chika2.jpg");
    }

    public static String encode(String imagePath) throws IOException {
        String base64Image = "";
        File file = new File(imagePath);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a Image file from file system
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);
            base64Image = Base64.getEncoder().encodeToString(imageData);
            message = Base64.getEncoder().encodeToString(imageData);
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
        List<String> lines = Arrays.asList(base64Image);
        Path txtfile = Paths.get("base64");
        Files.write(txtfile, lines, Charset.forName("UTF-8"));
        return base64Image;
    }

    public static void decode(String base64Image, String pathFile) {
        try (FileOutputStream imageOutFile = new FileOutputStream(pathFile)) {
            // Converting a Base64 String into Image byte array
            byte[] imageByteArray = Base64.getDecoder().decode(message);
            imageOutFile.write(imageByteArray);
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
    }
}
