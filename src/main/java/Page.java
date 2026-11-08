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
 

    
public boolean hasOverlap(
        NotebookEntry currentEntry,
        double proposedX,
        double proposedWidth,
        double pageWidth,
        double fontSize
) {
    double gap = 8.0;

    for (NotebookEntry other : entries) {

        // An entry should never collide with itself.
        if (other == currentEntry) {
            continue;
        }

        // Only entries on the same ruled line can collide.
        if (other.getLineIndex() != currentEntry.getLineIndex()) {
            continue;
        }

        double otherX = other.getXFraction() * pageWidth;

        // Estimate the other entry's rendered text width.
        javafx.scene.text.Text measurement =
                new javafx.scene.text.Text(other.getText());

        measurement.setFont(
                javafx.scene.text.Font.font(fontSize)
        );

        double otherWidth = Math.max(
                30,
                measurement.getLayoutBounds().getWidth() + 12
        );

        boolean overlapping =
                proposedX < otherX + otherWidth + gap &&
                proposedX + proposedWidth + gap > otherX;

        if (overlapping) {
            return true;
        }
    }

    return false;
}

public boolean canPlaceEntry(
        int lineIndex,
        double proposedX,
        double proposedWidth,
        double pageWidth,
        double fontSize
) {
    NotebookEntry candidate = new NotebookEntry(
            lineIndex,
            proposedX / pageWidth,
            ""
    );

    return !hasOverlap(
            candidate,
            proposedX,
            proposedWidth,
            pageWidth,
            fontSize
    );
}

    
}




