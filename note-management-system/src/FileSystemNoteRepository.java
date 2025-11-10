import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FileSystemNoteRepository implements NoteRepository {

    private final Path baseDir;

    public FileSystemNoteRepository(String baseDirName) {
        this.baseDir = Paths.get(baseDirName);
    }

    private Path titleToPath(String title) {
        // Ét filnavn pr. note
        return baseDir.resolve(title + ".txt");
    }

    @Override
    public void ensureStore() throws IOException {
        if (Files.notExists(baseDir)) {
            Files.createDirectories(baseDir);
        }
    }

    @Override
    public void save(String title, String content) throws IOException, DuplicateNoteException {
        ensureStore();
        Path target = titleToPath(title);
        if (Files.exists(target)) {
            throw new DuplicateNoteException("En note med titlen '" + title + "' findes allerede.");
        }

        // Sikker skrivning: tmp → move
        Path tmp = baseDir.resolve(".tmp-" + System.nanoTime() + ".txt");
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(Files.newOutputStream(tmp, StandardOpenOption.CREATE_NEW),
                                        StandardCharsets.UTF_8))) {
            if (content != null) {
                bw.write(content);
            }
        }

        try {
            Files.move(tmp, target, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            // Fallback hvis filsystemet ikke understøtter atomic move
            Files.move(tmp, target);
        } finally {
            // Hvis noget gik galt, så fjern tmp, hvis den stadig findes
            try {
                Files.deleteIfExists(tmp);
            } catch (IOException ignore) { /* best effort */ }
        }
    }

    @Override
    public String read(String title) throws IOException, NoteNotFoundException {
        ensureStore();
        Path p = titleToPath(title);
        if (Files.notExists(p) || !Files.isRegularFile(p)) {
            throw new NoteNotFoundException("Noten '" + title + "' blev ikke fundet.");
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Files.newInputStream(p), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    @Override
    public List<String> listTitles() throws IOException {
        ensureStore();
        List<String> titles = new ArrayList<>();
        try {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(baseDir, "*.txt")) {
                for (Path p : stream) {
                    String fileName = p.getFileName().toString();
                    String title = fileName.substring(0, fileName.length() - ".txt".length());
                    titles.add(title);
                }
            }
        } catch (DirectoryIteratorException e) {
            throw new IOException("Fejl ved læsning af note-mappe.", e);
        }
        return titles.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }
}
