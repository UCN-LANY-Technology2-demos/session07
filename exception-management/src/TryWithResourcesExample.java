import java.io.*;

public class TryWithResourcesExample {
	
    public static void main(String[] args) {
    
    	try (BufferedReader reader = new BufferedReader(new FileReader("missing.txt"))) {
            String line = reader.readLine();
            System.out.println("First line: " + line);
        
    	} catch (FileNotFoundException e) {
            System.out.println("Filen blev ikke fundet: " + e.getMessage());
        
    	} catch (IOException e) {
            System.out.println("Fejl ved læsning: " + e.getMessage());
        }
    }
}
