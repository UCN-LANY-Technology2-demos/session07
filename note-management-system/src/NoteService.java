import java.io.IOException;
import java.util.List;

public class NoteService {

    private final NoteRepository repo;

    public NoteService(NoteRepository repo) {
        this.repo = repo;
    }

    public String sanitizeTitle(String rawTitle) throws IllegalArgumentException {
        if (rawTitle == null) throw new IllegalArgumentException("Titlen må ikke være null.");
        String t = rawTitle.trim();
        if (t.isEmpty()) throw new IllegalArgumentException("Titlen må ikke være tom.");

        // Tillad bogstaver, tal, mellemrum, bindestreg og underscore. Erstat andet med '-'
        t = t.replaceAll("[^A-Za-z0-9 _-]", "-");

        // Komprimer whitespace til enkelt bindestreg
        t = t.replaceAll("\\s+", "-");

        // Trim bindestreger i ender
        t = t.replaceAll("^-+", "").replaceAll("-+$", "");

        if (t.isEmpty())
            throw new IllegalArgumentException("Titlen blev tom efter sanitering. Vælg en anden titel.");

        // Max-længde (kan justeres)
        int MAX = 64;
        if (t.length() > MAX) {
            t = t.substring(0, MAX);
        }
        return t;
    }

    public void createNote(String rawTitle, String content)
            throws IOException, DuplicateNoteException {
        String title = sanitizeTitle(rawTitle);
        repo.save(title, content == null ? "" : content);
    }

    public List<String> listNotes() throws IOException {
        return repo.listTitles();
    }

    public String getNote(String rawTitle) throws IOException, NoteNotFoundException {
        String title = sanitizeTitle(rawTitle);
        return repo.read(title);
    }
}
