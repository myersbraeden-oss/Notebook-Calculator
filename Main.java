public class Main{

    public static void main(String[] args) {

        // testing calculator logic
        double result = Calculator.calculate(3.9, "-", 9.9);

        if (result % 1 == 0) {
            System.out.println((int)result);
        } else {
            System.out.println(result);
        }


        // testing page and line storage
        Page page1 = new Page();
        page1.setLine(0, "2 + 3 = 5");
        System.out.println(page1.getLine(0));

    

    Notebook notebook = new Notebook();

    notebook.getCurrentPage().setLine(0, "2 + 3 = 5");

    notebook.nextPage();
    notebook.getCurrentPage().setLine(0, "10 * 2 = 20");

    System.out.println(notebook.getCurrentPageNumber());
    System.out.println(notebook.getCurrentPage().getLine(0));

    notebook.previousPage();

    System.out.println(notebook.getCurrentPageNumber());
    System.out.println(notebook.getCurrentPage().getLine(0));
    
    }
}
