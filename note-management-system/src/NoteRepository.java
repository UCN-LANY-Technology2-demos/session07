import java.io.IOException;
import java.util.List;

public interface NoteRepository {
    
	void ensureStore() throws IOException;
    
	void save(String title, String content) throws IOException, DuplicateNoteException;
    
	String read(String title) throws IOException, NoteNotFoundException;
    
    List<String> listTitles() throws IOException;
}
