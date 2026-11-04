import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Line;

public class NotebookApp extends Application {

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #fdfdf5;");
        
        for (int i = 0; i < 34; i++){
            double y = 100 + (i * 22);
            Line line = new Line(0, y, 900, y);
            line.endXProperty().bind(root.widthProperty());
            line.setStroke(Color.DARKBLUE);
            root.getChildren().add(line);
        }

        Line margLine = new Line(100, 0, 100, 892);
        margLine.setStroke(Color.RED);
        margLine.setStrokeWidth(2);
        root.getChildren().add(margLine);

        Scene scene = new Scene(root, 900, 850);

        stage.setTitle("Notebook Calculator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
