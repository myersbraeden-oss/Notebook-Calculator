import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;

public class NotebookApp extends Application {

    private Notebook notebook = new Notebook();

    @Override
    public void start(Stage stage) {

        Pane root = new Pane();
        root.setStyle("-fx-background-color: #000000df;");
        
        for (int i = 0; i < 36; i++){
            double fraction = i / 35.0;

            Line line = new Line();
            line.startXProperty().set(0);
            line.endXProperty().bind(root.widthProperty());
            
            line.startYProperty().bind(
                root.heightProperty()
                .multiply(0.85)
                .multiply(fraction)
                .add(root.heightProperty().multiply(0.10))
            );

            line.endYProperty().bind(line.startYProperty());

            line.setStroke(Color.GRAY);
            line.setStrokeWidth(0.30);
            root.getChildren().add(line);
        }

        Line margLine = new Line();

        margLine.startXProperty().bind(root.widthProperty().multiply(0.10));
        margLine.endXProperty().bind(margLine.startXProperty());

        margLine.setStartY(0);
        margLine.endYProperty().bind(root.heightProperty());

        margLine.setStroke(Color.PERU);
        margLine.setStrokeWidth(0.65);
        root.getChildren().add(margLine);


   

    
root.setOnMouseClicked(event -> {

    if (event.getButton() != MouseButton.SECONDARY) {
        return;
    }

    double x = event.getX();
    double y = event.getY();

    double pageWidth = root.getWidth();
    double pageHeight = root.getHeight();

    if (pageWidth <= 0 || pageHeight <= 0) {
        return;
    }

    // Find the nearest notebook line.
    double topMargin = pageHeight * 0.10;
    double lineSpacing = pageHeight * 0.85 / 35.0;

    int lineIndex = (int) Math.round(
            (y - topMargin) / lineSpacing
    );

    lineIndex = Math.max(0, Math.min(35, lineIndex));

    // Initial field width and current font size.
    double initialWidth = 30.0;
    double fontSize = pageHeight * 0.017;

    Page page = notebook.getCurrentPage();

    // Reject placement if another field occupies this space.
    if (!page.canPlaceEntry(
        lineIndex,
        x,
        initialWidth,
        pageWidth,
        fontSize
)) {
    System.out.println(
        "Placement rejected at X: " + x +
        ", line: " + lineIndex
    );
    return;
}

System.out.println(
    "Placement accepted at X: " + x +
    ", line: " + lineIndex
);

    double xFraction = x / pageWidth;

    NotebookEntry entry = new NotebookEntry(
            lineIndex,
            xFraction,
            ""
    );

    page.addEntry(entry);

    TextField input = new TextField();

    // Transparent input that scales with notebook height.
    input.styleProperty().bind(
            javafx.beans.binding.Bindings.format(
                    "-fx-background-color: transparent;" +
                    "-fx-border-color: transparent;" +
                    "-fx-text-fill: #E0E0E0;" +
                    "-fx-padding: 0;" +
                    "-fx-font-size: %.2fpx;",
                    root.heightProperty().multiply(0.017)
            )
    );

    // Measure the text so the field grows horizontally.
    Text measurement = new Text();

    measurement.textProperty().bind(input.textProperty());
    measurement.fontProperty().bind(input.fontProperty());

    input.prefWidthProperty().bind(
            measurement.layoutBoundsProperty().map(
                    bounds -> Math.max(
                            30,
                            bounds.getWidth() + 12
                    )
            )
    );

    // Lock horizontal position to its stored fraction.
    input.layoutXProperty().bind(
            root.widthProperty().multiply(
                    entry.getXFraction()
            )
    );

    // Snap the bottom of the field to its ruled line.
    input.layoutYProperty().bind(
            root.heightProperty()
                    .multiply(
                            0.10 +
                            0.85 * entry.getLineIndex() / 35.0
                    )
                    .subtract(input.heightProperty())
    );

    // Prevent text from growing into another entry.
    input.textProperty().addListener(
            (observable, oldText, newText) -> {

                double currentFontSize =
                        root.getHeight() * 0.017;

                Text proposedMeasurement = new Text(newText);

                proposedMeasurement.setFont(
                        input.getFont()
                );

                double proposedWidth = Math.max(
                        30,
                        proposedMeasurement
                                .getLayoutBounds()
                                .getWidth() + 12
                );

                double proposedX =
                        entry.getXFraction() * root.getWidth();

                boolean overlaps = page.hasOverlap(
                        entry,
                        proposedX,
                        proposedWidth,
                        root.getWidth(),
                        currentFontSize
                );

                if (overlaps) {

                    // Restore the previous valid text.
                    input.setText(oldText);

                    return;
                }

                // Save the accepted text to the model.
                entry.setText(newText);
            }

            
    );

    
input.textProperty().addListener(
    (observable, oldText, newText) -> {

        // Only calculate when the user types "=" at the end.
        if (!newText.endsWith("=")) {
            return;
        }

        // Remove the "=" before evaluating.
        String expression = newText.substring(
                0,
                newText.length() - 1
        ).trim();

        if (expression.isEmpty()) {
            return;
        }

        try {
            double result = Calculator.calculateExpression(
                    expression
            );

            String completedText =
                    expression + " = " + formatResult(result);

            // Replace the typed expression with its result.
            input.setText(completedText);

            // Keep the cursor at the end.
            input.positionCaret(completedText.length());

        } catch (IllegalArgumentException exception) {
        System.out.println("Cannot calculate: " + expression);
        } catch (ArithmeticException exception) {
        System.out.println("Arithmetic error: " + exception.getMessage());
        }
    }
);


    root.getChildren().add(input);

    input.requestFocus();



    System.out.println("Right-click at X: " + x + ", Y: " + y);{

    }}
);
        
        ScrollPane scrollPane = new ScrollPane(root);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        
        Scene scene = new Scene(scrollPane, 900, 700);

        stage.setTitle("Notebook Calculator");
        stage.setScene(scene);

        stage.setMinWidth(350);
        stage.setMinHeight(400);

        stage.show();
    }


    
private String formatResult(double result) {

    if (!Double.isFinite(result)) {
        return Double.toString(result);
    }

    // Display whole-number results without ".0".
    if (result == Math.rint(result)
            && result >= Long.MIN_VALUE
            && result <= Long.MAX_VALUE) {

        return Long.toString((long) result);
    }

    return Double.toString(result);
}


    public static void main(String[] args) {
        launch(args);
    }
}
