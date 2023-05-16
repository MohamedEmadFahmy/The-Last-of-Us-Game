package controller;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import views.characterSelect;
import views.mainMenu;

public class Controller extends Application {

    @Override
    public void start(Stage primaryStage) {

        // primaryStage.setFullScreen(true);
        // primaryStage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("q"));
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setTitle("The Game");

        // Scene scene = new Scene(root, 300, 250);
        // Scene scene = new Scene(new GridPane(), Color.BLACK);
        // Scene scene = new Scene(root, 600, 600);
        // Scene scene = new Scene(new TilePane(), 600, 600, Color.AQUAMARINE);
        Parent root2 = null;
        Scene scene = null;
        try {
            root2 = FXMLLoader
                    .load(getClass().getResource("helloWorld.fxml"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        scene = new Scene(root2, 600, 600, Color.AQUAMARINE);

        // Import scene from mainMenu Class in views
        mainMenu mainMenu = new mainMenu(primaryStage);
        Scene mainMenuScene = mainMenu.scene;
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();

        characterSelect characterSelect = new characterSelect(primaryStage);
        Scene selectCharacterScene = characterSelect.scene;
        primaryStage.setScene(selectCharacterScene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
