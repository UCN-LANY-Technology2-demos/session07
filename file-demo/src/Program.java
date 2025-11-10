import java.io.File;
import java.text.SimpleDateFormat;

public class Program {
	
    public static void main(String[] args) {
    
        File file = new File("."); // "." = current directory

        System.out.println("Analyserer sti: " + file.getAbsolutePath());
        System.out.println("----------------------------------");

        // Eksistens og type
        System.out.println("Eksisterer? " + file.exists());
        System.out.println("Er fil? " + file.isFile());
        System.out.println("Er mappe? " + file.isDirectory());

        // Egenskaber
        System.out.println("Navn: " + file.getName());
        System.out.println("Sti: " + file.getPath());
        System.out.println("Absolut sti: " + file.getAbsolutePath());
        System.out.println("Kan læses? " + file.canRead());
        System.out.println("Kan skrives? " + file.canWrite());
        System.out.println("Sidst ændret: " +
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
        System.out.println("Størrelse: " + file.length() + " bytes");

        // Hvis mappe – list filer
        if (file.isDirectory()) {
            System.out.println("\nIndhold:");
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    System.out.printf(" - %-30s (%s)\n",
                        f.getName(),
                        f.isDirectory() ? "dir" : "file");
                }
            }
        }
    }
}

