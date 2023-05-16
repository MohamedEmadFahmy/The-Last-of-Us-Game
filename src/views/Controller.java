package views;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Controller extends Application {
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    Font font = Font.loadFont(this.getClass().getResourceAsStream("/views/fonts/The Bomb Sound.ttf"), 40);

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setHeight(screenHeight);
        primaryStage.setWidth(screenWidth);
        primaryStage.setFullScreen(true);
        // primaryStage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("esc"));
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.getIcons().add(new Image("file:src/views/imgs/icon.jpg"));
        primaryStage.setTitle("The Game");
        switchToMainMenu(primaryStage);
    }

    public void switchToMainMenu(Stage primaryStage) {
        // primaryStage.initStyle(StageStyle.TRANSPARENT);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, screenHeight, screenWidth);
        String mainMenuCSS = this.getClass().getResource("/views/styles/mainMenu.css").toExternalForm();
        scene.getStylesheets().add(mainMenuCSS);
        ImagePattern pattern = new ImagePattern(new Image("/views/imgs/bgfinal.png"));
        scene.setFill(pattern);

        Button startGameBtn = new Button("Start game");

        startGameBtn.setTranslateY(-screenHeight / 3.5);
        startGameBtn.setTranslateX(screenWidth / 20);
        root.setBottom(startGameBtn);

        root.setBackground(null);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // public void switchToCharacterSelect(Stage primaryStage) {
    // TilePane root = new TilePane();
    // root.setAlignment(Pos.CENTER);
    // root.setPrefRows(2);
    // root.setPrefTileWidth(130);
    // root.setPrefTileHeight(150);
    // // root.getChildren().add(btn);
    // for (int i = 0; i < 8; i++) {
    // Button btn = new Button();
    // btn.setText("Button " + (i + 1));
    // btn.setMinHeight(100);
    // btn.setMinWidth(110);
    // // btn.scaleYProperty(100);
    // root.getChildren().add(btn);

    // btn.setOnAction(new EventHandler<ActionEvent>() {

    // @Override
    // public void handle(ActionEvent event) {
    // System.out.println("Hello World!");
    // primaryStage.close();
    // }
    // });
    // }
    // Scene scene = new Scene(root, 1920, 1040);
    // primaryStage.setScene(scene);
    // primaryStage.show();
    // }
}
