import java.util.ArrayList;

public class Page {

    private ArrayList<NotebookEntry> entries = new ArrayList<>();

    public void addEntry(NotebookEntry entry) {
        entries.add(entry);
    }

    public ArrayList<NotebookEntry> getEntries() {
        return entries;
    }
    public void removeEntry(NotebookEntry entry) {
        entries.remove(entry);
    }
 
    
}


