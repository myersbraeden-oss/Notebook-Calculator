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
        if (event.getButton() == MouseButton.SECONDARY) {
            
            double x = event.getX();
            double y = event.getY();

            // Calculate the nearest ruled line
            double height = root.getHeight();
            double topMargin = height * 0.10;
            double lineSpacing = (height * 0.85) / 35.0;

            int lineIndex = (int) Math.round(
            (y - topMargin) / lineSpacing
            );

            // Keep the index between 0 and 35
            lineIndex = Math.max(0, Math.min(35, lineIndex));

            // Calculate proportional horizontal position
            double xFraction = x / root.getWidth();

            // Create and store the entry
            NotebookEntry entry = new NotebookEntry(
                lineIndex,
                xFraction,
                ""
            );

            notebook.getCurrentPage().addEntry(entry);

            // Create the visible text field
            TextField input = new TextField();
            
                input.styleProperty().bind(
                    javafx.beans.binding.Bindings.format(
                        "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;" +
                        "-fx-text-fill: #E0E0E0;" +
                        "-fx-padding: 0;" +
                        "-fx-font-size: %.2fpx;",
                        root.heightProperty().multiply(0.022)
                    )
                );

                input.layoutXProperty().bind(
                    root.widthProperty().multiply(entry.getXFraction())
                );

                input.layoutYProperty().bind(
                    root.heightProperty()
                        .multiply(0.10 + 0.85 * entry.getLineIndex() / 35.0)
                        .subtract(input.heightProperty())
                );

                // Keep the entry synchronized with what the user types
                input.textProperty().addListener((observable, oldText, newText) -> {
                entry.setText(newText);
                });

                Text measurement = new Text();

                measurement.textProperty().bind(input.textProperty());
                measurement.fontProperty().bind(input.fontProperty());

                input.prefWidthProperty().bind(
                    measurement.layoutBoundsProperty()
                        .map(bounds -> Math.max(30, bounds.getWidth() + 12))
                );

                root.getChildren().add(input);

                input.requestFocus();

                System.out.println("Right-click at X: " + x + ", Y: " + y);
        }
        }
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

    public static void main(String[] args) {
        launch(args);
    }
}
