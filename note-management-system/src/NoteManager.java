import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class NoteManager {

    private static final String NOTES_DIR = "notes";

    public static void main(String[] args) {
        NoteRepository repo = new FileSystemNoteRepository(NOTES_DIR);
        NoteService service = new NoteService(repo);

        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("=== Simple Note Management System ===");
            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Create a new note");
                System.out.println("2. List notes");
                System.out.println("3. Read a note");
                System.out.println("4. Exit");
                System.out.print("Choose (1-4): ");
  
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "1":
                        handleCreate(sc, service);
                        break;
                    case "2":
                        handleList(service);
                        break;
                    case "3":
                        handleRead(sc, service);
                        break;
                    case "4":
                        System.out.println("Bye!");
                        return;
                    default:
                        System.out.println("Unknown choice. Try again.");
                }
            }
        }
    }

    private static void handleCreate(Scanner sc, NoteService service) {
        try {
            System.out.print("Enter note title: ");
            String rawTitle = sc.nextLine();

            System.out.println("Enter note text (end with an empty line):");
            StringBuilder content = new StringBuilder();
            while (true) {
                String line = sc.nextLine();
                if (line.isEmpty()) break;
                content.append(line).append(System.lineSeparator());
            }

            service.createNote(rawTitle, content.toString());
            System.out.println("Note saved.");

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid title: " + e.getMessage());
        } catch (DuplicateNoteException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error while saving note: " + e.getMessage());
        }
    }

    private static void handleList(NoteService service) {
        try {
            List<String> titles = service.listNotes();
            if (titles.isEmpty()) {
                System.out.println("No notes found.");
                return;
            }
            System.out.println("\nAvailable notes:");
            for (int i = 0; i < titles.size(); i++) {
                System.out.printf("%2d. %s%n", i + 1, titles.get(i));
            }
        } catch (IOException e) {
            System.out.println("I/O error while listing notes: " + e.getMessage());
        }
    }

    private static void handleRead(Scanner sc, NoteService service) {
        try {
            List<String> titles = service.listNotes();
            if (titles.isEmpty()) {
                System.out.println("No notes found.");
                return;
            }
            for (int i = 0; i < titles.size(); i++) {
                System.out.printf("%2d. %s%n", i + 1, titles.get(i));
            }
            System.out.print("Select note by number or type a title: ");
            String sel = sc.nextLine().trim();

            String title;
            if (sel.matches("\\d+")) {
                int idx = Integer.parseInt(sel) - 1;
                if (idx < 0 || idx >= titles.size()) {
                    System.out.println("Invalid selection.");
                    return;
                }
                title = titles.get(idx);
            } else {
                title = sel; // vil blive saniteret i service.getNote
            }

            String content = service.getNote(title);
            System.out.println("\n=== " + title + " ===");
            System.out.print(content);

        } catch (IOException e) {
            System.out.println("I/O error while reading note: " + e.getMessage());
        } catch (NoteNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid title: " + e.getMessage());
        }
    }
}
