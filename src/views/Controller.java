package views;

import engine.Game;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.characters.Hero;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Queue;

public class Controller extends Application {
    static int index = 1;
    static Media hover = new Media(new File("src/views/sounds/click.wav").toURI().toString());
    static Media click = new Media(new File("src/views/sounds/mouse_click.wav").toURI().toString());
    static Media main = new Media(new File("src/views/sounds/maintheme.mp3").toURI().toString());
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

    private void play(Media media) {
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.3);
        mediaPlayer.play();
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
        ScaleTransition st = new ScaleTransition(Duration.millis(30), startGameBtn);
        st.setCycleCount(1);
        st.setInterpolator(Interpolator.EASE_BOTH);
        play(main);
        startGameBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                startGameBtn.getStyleClass().add("hover");
                st.setToX(1.05);
                st.setToY(1.05);
                st.playFromStart();
                play(hover);
            }
        });

        startGameBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                startGameBtn.getStyleClass().remove("hover");
                st.setToX(1);
                st.setToY(1);
                st.playFromStart();
            }
        });
        startGameBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                play(click);
                try {
                    switchToCharacterSelect(primaryStage);
                }
                catch (Exception e) {
                    System.out.print("something went wrong.");
                }
            }
        });

        startGameBtn.setTranslateY(-screenHeight / 3.5);
        startGameBtn.setTranslateX(screenWidth / 25);
        root.setBottom(startGameBtn);

        Button exitGameBtn = new Button("EXIT GAME");
        ScaleTransition st2 = new ScaleTransition(Duration.millis(30), exitGameBtn);
        exitGameBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                exitGameBtn.getStyleClass().add("hover");
                st2.setToX(1.05);
                st2.setToY(1.05);
                st2.playFromStart();
                play(hover);
            }
        });

        exitGameBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                exitGameBtn.getStyleClass().remove("hover");
                st2.setToX(1);
                st2.setToY(1);
                st2.playFromStart();
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

    public void switchToCharacterSelect(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();
        Scene scene = primaryStage.getScene();
        scene.setRoot(root);
        Button leftSel = new Button();
        Button rightChar = new Button();
        Button leftChar = new Button();
        Button middleChar = new Button();
        Button rightSel = new Button();
        HBox Characters = new HBox();
        Characters.getChildren().addAll(leftSel,leftChar,middleChar,rightChar,rightSel);
        Characters.setAlignment(Pos.CENTER);
        root.getChildren().add(Characters);

        Game.loadHeroes("src/CSV files/Heros.csv");
        ArrayList<Hero> current = new ArrayList<Hero>();

        ArrayList<Image> imageArray = new ArrayList<Image>();
        imageArray.add(new Image("/views/imgs/Joel.png", 100, 100, false, false));
        imageArray.add(new Image("/views/imgs/Ellie.png", 100, 100, false, false));
        imageArray.add(new Image("/views/imgs/tess.png", 100, 100, false, false));
        imageArray.add(new Image("/views/imgs/4.png", 100, 100, false, false));
        imageArray.add(new Image("/views/imgs/5.png", 100, 100, false, false));
        imageArray.add(new Image("/views/imgs/6.png", 100, 100, false, false));
        imageArray.add(new Image("/views/imgs/7.png", 100, 100, false, false));
        imageArray.add(new Image("/views/imgs/8.png", 100, 100, false, false));

        rightChar.setGraphic(new ImageView(imageArray.get(2)));
        middleChar.setGraphic(new ImageView(imageArray.get(1)));
        leftChar.setGraphic(new ImageView(imageArray.get(0)));
        leftSel.setGraphic(new ImageView(new Image("/views/imgs/arrowleft.png", 100, 100, false, false)));
        rightSel.setGraphic(new ImageView(new Image("/views/imgs/arrowright.png", 100, 100, false, false)));

        Label Name = new Label("Name: ");
        Label Class = new Label("Class: ");
        Label MaxHp = new Label("Max Health: ");
        Label ActionPoints = new Label("Action Points: ");
        Label Damage = new Label("Attack Damage: ");
        Label CharSelect = new Label("SELECT YOUR CHARACTER");

        Name.setTranslateY(screenHeight - (screenHeight*0.8));
        Class.setTranslateY(screenHeight  - (screenHeight*0.75));
        MaxHp.setTranslateY(screenHeight - (screenHeight*0.70));
        ActionPoints.setTranslateY(screenHeight - (screenHeight*0.65));
        Damage.setTranslateY(screenHeight - (screenHeight*0.60));
        CharSelect.setTranslateY(-screenHeight*0.30);


        root.getChildren().addAll(Name,Class,MaxHp,ActionPoints,Damage,CharSelect);


        for (int i = 0; i < Game.availableHeroes.size(); i++) {
            current.add(Game.availableHeroes.get(i));
        }
        ScaleTransition st = new ScaleTransition(Duration.millis(30), rightChar);
        ScaleTransition st2 = new ScaleTransition(Duration.millis(30), leftChar);
        ScaleTransition st3 = new ScaleTransition(Duration.millis(30), middleChar);
        rightChar.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                st.setToX(1.05);
                st.setToY(1.05);
                st.playFromStart();
                Name.setText("Name: " + current.get(index + 1).getName());
                Class.setText("Class: " + current.get(index + 1).getClass().getSimpleName());
                MaxHp.setText("Max Health: " + current.get(index + 1).getMaxHp());
                ActionPoints.setText("Max Actions: " + current.get(index + 1).getMaxActions());
                Damage.setText("Damage: " + current.get(index + 1).getAttackDmg());
                play(hover);
            }
        });

        rightChar.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                st.setToX(1);
                st.setToY(1);
                st.playFromStart();
            }
        });
        leftChar.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                st2.setToX(1.05);
                st2.setToY(1.05);
                st2.playFromStart();
                Name.setText("Name: " + current.get(index - 1).getName());
                Class.setText("Class: " + current.get(index - 1).getClass().getSimpleName());
                MaxHp.setText("Max Health: " + current.get(index - 1).getMaxHp());
                ActionPoints.setText("Max Actions: " + current.get(index - 1).getMaxActions());
                Damage.setText("Damage: " + current.get(index - 1).getAttackDmg());
                play(hover);
            }
        });

        leftChar.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                st2.setToX(1);
                st2.setToY(1);
                st2.playFromStart();
            }
        });
        middleChar.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                st3.setToX(1.05);
                st3.setToY(1.05);
                st3.playFromStart();
                Name.setText("Name: " + current.get(index).getName());
                Class.setText("Class: " + current.get(index).getClass().getSimpleName());
                MaxHp.setText("Max Health: " + current.get(index).getMaxHp());
                ActionPoints.setText("Max Actions: " + current.get(index).getMaxActions());
                Damage.setText("Damage: " + current.get(index).getAttackDmg());
                play(hover);
            }
        });

        middleChar.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                st3.setToX(1);
                st3.setToY(1);
                st3.playFromStart();
            }
        });
        leftSel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (index > 1) {
                    updateImages(--index, rightChar, middleChar, leftChar, imageArray);
                }
            }
        });
        rightSel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (index < 6) {
                    updateImages(++index,rightChar,middleChar,leftChar,imageArray);
                }
            }
        });
        leftChar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (index < 6) {
                    updateImages(++index,rightChar,middleChar,leftChar,imageArray);
                }
            }
        });
        leftChar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                // start game with ArrayList current index - 1
            }
        });
        rightChar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                // start game with ArrayList current index + 1
            }
        });
        middleChar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                // start game with ArrayList current index
            }
        });
        root.setBackground(null);
        primaryStage.show();
    }
    public void updateImages(int i,Button rightChar,Button middleChar,Button leftChar, ArrayList<Image> imageArray) {
        rightChar.setGraphic(new ImageView(imageArray.get(index + 1)));
        middleChar.setGraphic(new ImageView(imageArray.get(index)));
        leftChar.setGraphic(new ImageView(imageArray.get(index - 1)));
    }
}
