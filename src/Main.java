import IO.ReaderWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] arg) {

        Blowfish blowfish = new Blowfish();
        blowfish.generateKey("eugene orlov".getBytes());

        // Изображение
        byte[] imageArray = blowfish.alignment(ReaderWriter.readJpg("lena.jpg"), ' ');
        ReaderWriter.writeJpg("source.jpg", imageArray);

        blowfish.encrypt(imageArray);
        ReaderWriter.writeJpg("encrypted.jpg", imageArray);

        blowfish.decrypt(imageArray);
        ReaderWriter.writeJpg("decrypted.jpg", imageArray);


        // Текст
        byte[] textArray = blowfish.alignment(ReaderWriter.read("input.txt").getBytes(), ' ');
        System.out.println("Source message " + new String(textArray));

        // Input byte array for correlation
        byte[] input = textArray;

        blowfish.encrypt(textArray);
        ReaderWriter.write("encrypted.txt", new String(textArray));

        // Encrypted byte array for correlation
        byte[] decrypted = textArray;

        // Calculating correlation
        System.out.println("Correlation: " + Blowfish.calculateCorrelaion(input, decrypted));

        blowfish.decrypt(textArray);
        ReaderWriter.write("decrypted.txt", new String(textArray));
    }

}
