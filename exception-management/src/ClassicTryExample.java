import java.io.*;

public class ClassicTryExample {
	
    public static void main(String[] args) {
    
    	BufferedReader reader = null;
        
    	try {
            reader = new BufferedReader(new FileReader("missing.txt"));
            String line = reader.readLine();
            System.out.println("First line: " + line);
        
    	} catch (FileNotFoundException e) {
           	System.out.println("Filen blev ikke fundet: " + e.getMessage());
        
        } catch (IOException e) {
            System.out.println("Fejl ved læsning: " + e.getMessage());
        
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                System.out.println("Kunne ikke lukke streamen.");
            }
        }
    }
}
