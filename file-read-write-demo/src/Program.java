import java.io.*;

public class Program {
	
    public static void main(String[] args) {
    
    	String inputFile = "input.txt";
        String outputFile = "output.txt";

        // Læs fra fil og skriv til en ny fil
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Læs linje: " + line);
                writer.write(line.toUpperCase());  // Gør noget med data
                writer.newLine();                  // Tilføj linjeskift
            }

            System.out.println("\nFilen er skrevet til: " + outputFile);

        } catch (FileNotFoundException e) {
            System.out.println("Filen " + inputFile + " blev ikke fundet.");
        } catch (IOException e) {
            System.out.println("Der opstod en I/O-fejl: " + e.getMessage());
        }
    }
}
