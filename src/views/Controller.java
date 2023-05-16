package views;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Controller extends Application {

    @Override
    public void start(Stage primaryStage) {

        // primaryStage.setFullScreen(true);
        // primaryStage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("q"));
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setTitle("The Game");
        switchToMainMenu(primaryStage);
        switchToCharacterSelect(primaryStage);

    }

    public void switchToMainMenu(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 600, 600, Color.AQUAMARINE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void switchToCharacterSelect(Stage primaryStage) {
        TilePane root = new TilePane();
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
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
