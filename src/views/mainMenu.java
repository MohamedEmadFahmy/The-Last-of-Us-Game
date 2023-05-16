package views;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
// 

public class mainMenu extends Application {
    public Group root = new Group();
    public Scene scene;

    public mainMenu(Stage mainStage) {
        scene = new Scene(root, 600, 600, Color.AQUAMARINE);
    }

    @Override
    public void start(Stage arg0) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'start'");
    }
}
