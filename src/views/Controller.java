package views;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Controller extends Application {

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("q"));
        primaryStage.getIcons().add();
        primaryStage.setTitle("The Game");
        try {
            switchToMainMenu(primaryStage);
        }
        catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public void switchToMainMenu(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1920, 1080);
        ImagePattern pattern = new ImagePattern(new Image("file:src/views/imgs/bgfinal.png"));
        scene.setFill(pattern);
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
