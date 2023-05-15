package views;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class characterSelect extends Application {
    public TilePane root = new TilePane();

    public Scene scene;

    public characterSelect(Stage primaryStage) {
        // GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setPrefRows(2);
        root.setPrefTileWidth(130);
        root.setPrefTileHeight(150);
        // root.getChildren().add(btn);
        for (int i = 0; i < 8; i++) {
            Button btn = new Button();
            btn.setText("Button " + (i + 1));
            btn.setMinHeight(100);
            btn.setMinWidth(110);
            // btn.scaleYProperty(100);
            root.getChildren().add(btn);

            btn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Hello World!");
                    primaryStage.close();
                }
            });
        }
        scene = new Scene(root, 600, 600);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'start'");
    }
}
