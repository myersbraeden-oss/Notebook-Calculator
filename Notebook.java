
import java.util.ArrayList;

public class Notebook {

    private ArrayList<Page> pages = new ArrayList<>();
    private int currentPage = 0;

    public Notebook() {
        pages.add(new Page());
    }

    public Page getCurrentPage() {
        return pages.get(currentPage);
    }

    public void nextPage() {
        if (currentPage == pages.size() - 1) {
            pages.add(new Page());
        }

        currentPage++;
    }

    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
        }
    }

    public int getCurrentPageNumber() {
        return currentPage + 1;
    }

    public int getPageCount() {
        return pages.size();
    }
}
