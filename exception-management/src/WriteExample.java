import java.io.*;

public class WriteExample {
    public static void main(String[] args) {
        String text = "Dette er en test af try-with-resources.";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Windows\\System32\\test.txt"))) {
            writer.write(text);
            System.out.println("Filen er skrevet succesfuldt.");
        } catch (IOException e) {
            System.out.println("Fejl under skrivning: " + e);
        }
    }
}
