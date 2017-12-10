package IO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

public class ReaderWriter {

    public static void write(String filename, String text) {
        try {
            Writer writer = new FileWriter("/Users/Eugene/Desktop/java_projects/Blowfish/src/Files/" + filename);
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public static String read(String filename) {
        String text = "";
        try {
            Reader reader = new FileReader("/Users/Eugene/Desktop/java_projects/Blowfish/src/Files/" + filename);
            Scanner scanner = new Scanner(reader);
            while (scanner.hasNextLine()) {
                text += scanner.nextLine();
                text += "\n";
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        return text;
    }

    public static byte[] readJpg(String imageName) {
        try {
            File f = new File("/Users/Eugene/Desktop/java_projects/Blowfish/src/Files/" + imageName);
            BufferedImage originalImage = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            System.out.println("Failed to read image file: " + e);
        }

        return null;
    }

    public static void writeJpg(String imageName, byte[] bytes) {
        try (FileOutputStream fos = new FileOutputStream("/Users/Eugene/Desktop/java_projects/Blowfish/src/Files/" + imageName)) {
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            System.out.println("Failed to write image file: " + e);
        }
    }

}
