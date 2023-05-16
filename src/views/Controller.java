package views;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Controller extends Application {
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    Font font = Font.loadFont(this.getClass().getResourceAsStream("/views/fonts/The Bomb Sound.ttf"), 40);
    Font font2 = Font.loadFont(this.getClass().getResourceAsStream("/views/fonts/Aka-AcidGR-Compacta.ttf"), 40);

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

        Button startGameBtn = new Button("START GAME");
        startGameBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                startGameBtn.getStyleClass().add("hover");
            }
        });

        startGameBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                startGameBtn.getStyleClass().remove("hover");
            }
        });
        startGameBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                System.out.println("Switch to character selection scene");
            }
        });

        startGameBtn.setTranslateY(-screenHeight / 3.5);
        startGameBtn.setTranslateX(screenWidth / 25);
        root.setBottom(startGameBtn);

        Button exitGameBtn = new Button("EXIT GAME");
        exitGameBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                exitGameBtn.getStyleClass().add("hover");
            }
        });

        exitGameBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                exitGameBtn.getStyleClass().remove("hover");
            }
        });
        exitGameBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                primaryStage.close();
            }
        });

        exitGameBtn.setTranslateY(screenHeight - (screenHeight / 5));
        exitGameBtn.setTranslateX(screenWidth / 37);
        root.setTop(exitGameBtn);

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
