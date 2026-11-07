public class NotebookEntry {

    private int lineIndex;
    private double xFraction;
    private String text;

    public NotebookEntry(int lineIndex, double xFraction, String text) {
        this.lineIndex = lineIndex;
        this.xFraction = xFraction;
        this.text = text;
    }

    public String getText() {
    return text;
    }

    public void setText(String text) {
    this.text = text;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    public double getXFraction() {
        return xFraction;
    }

    public void setXFraction(double xFraction) {
        this.xFraction = xFraction;
    }

}
