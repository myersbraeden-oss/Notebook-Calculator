public class Page {

    public String[] lines = new String[25];


    public String[] getLines() {
        return lines;
    }

    public String getLine(int index) {
        return lines[index];
    }

    public void setLine(int index, String text){
        lines[index] = text;
    }
    
}


