package views;

import engine.Game;
import exceptions.MovementException;
import exceptions.NotEnoughActionsException;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.collectibles.Vaccine;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.characters.Hero;
import model.characters.Character;
import model.characters.Direction;

import java.io.File;
import java.util.ArrayList;

public class Controller extends Application {
    static Hero currentHero = null;
    static Character currentTarget = null;
    static int index = 1;
    static Media hover = new Media(new File("src/views/sounds/click.wav").toURI().toString());
    static Media click = new Media(new File("src/views/sounds/mouse_click.wav").toURI().toString());
    static Media main = new Media(new File("src/views/sounds/maintheme.mp3").toURI().toString());
    static boolean playing = false;
    static ArrayList<Hero> current = new ArrayList<Hero>();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    Font font = Font.loadFont(this.getClass().getResourceAsStream("/views/fonts/The Bomb Sound.ttf"), 40);
    Font font2 = Font.loadFont(this.getClass().getResourceAsStream("/views/fonts/Aka-AcidGR-Compacta.ttf"), 40);

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setFullScreen(true);
        primaryStage.setHeight(screenHeight);
        primaryStage.setWidth(screenWidth);
        StackPane temp = new StackPane();
        // primaryStage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("esc"));
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.getIcons().add(new Image("file:src/views/imgs/icon.jpg"));
        primaryStage.setTitle("The Last Of Us Legacy");
        Scene scene = new Scene(temp, screenHeight, screenWidth);
        primaryStage.setScene(scene);
        switchToMainMenu(primaryStage);
        Game.loadHeroes("src/CSV files/Heros.csv");
        for (int i = 0; i < Game.availableHeroes.size(); i++) {
            current.add(Game.availableHeroes.get(i));
        }
    }

    private void play(Media media) {
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.3);
        mediaPlayer.play();
    }

    public void updateImages(int i, Label rightChar, Label middleChar, Label leftChar, ArrayList<Image> imageArray) {
        rightChar.setGraphic(new ImageView(imageArray.get(i + 1)));
        middleChar.setGraphic(new ImageView(imageArray.get(i)));
        leftChar.setGraphic(new ImageView(imageArray.get(i - 1)));
    }

    public void updateLabels(Label Name, Label Class, Label MaxHp, Label ActionPoints, Label Damage,
            ArrayList<Hero> current, int index) {
        Name.setText("Name: " + current.get(index).getName());
        Class.setText("Class: " + current.get(index).getClass().getSimpleName());
        MaxHp.setText("Max Health: " + current.get(index).getMaxHp());
        ActionPoints.setText("Max Actions: " + current.get(index).getMaxActions());
        Damage.setText("Damage: " + current.get(index).getAttackDmg());
    }

    public void switchToMainMenu(Stage primaryStage) {
        // primaryStage.initStyle(StageStyle.TRANSPARENT);
        BorderPane root = new BorderPane();
        Scene scene = primaryStage.getScene();
        scene.setRoot(root);
        String mainMenuCSS = this.getClass().getResource("/views/styles/mainMenu.css").toExternalForm();
        scene.getStylesheets().add(mainMenuCSS);
        ImagePattern pattern = new ImagePattern(new Image("/views/imgs/bgfinal.png"));
        scene.setFill(pattern);
        Button startGameBtn = new Button("START GAME");
        ScaleTransition st = new ScaleTransition(Duration.millis(30), startGameBtn);
        st.setCycleCount(1);
        st.setInterpolator(Interpolator.EASE_BOTH);
        if (playing == false) {
            play(main);
            playing = true;
        }

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
                } catch (Exception e) {
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

    public void switchToCharacterSelect(Stage primaryStage) {
        StackPane root = new StackPane();
        Scene scene = primaryStage.getScene();
        scene.setRoot(root);
        Button leftSel = new Button();
        leftSel.setMaxSize(200, 200);
        Label rightChar = new Label();
        Label leftChar = new Label();
        Label middleChar = new Label();
        Button rightSel = new Button();
        Button Continue = new Button("Continue");
        Button backToMenu = new Button("Back");
        rightSel.setMaxSize(200, 200);
        HBox Characters = new HBox(100);
        Characters.getChildren().addAll(leftSel, leftChar, middleChar, rightChar, rightSel);
        Characters.setAlignment(Pos.CENTER);

        root.getChildren().addAll(Characters, Continue, backToMenu);

        Continue.setTranslateY(screenHeight * 0.40);
        Continue.setTranslateX(screenWidth * 0.40);
        backToMenu.setTranslateY(screenHeight * 0.40);
        backToMenu.setTranslateX(-screenWidth * 0.40);

        ArrayList<Image> imageArray = new ArrayList<Image>();
        imageArray.add(new Image("/views/imgs/Joel.png", 200, 200, false, false));
        imageArray.add(new Image("/views/imgs/Ellie.png", 200, 200, false, false));
        imageArray.add(new Image("/views/imgs/tess.png", 200, 200, false, false));
        imageArray.add(new Image("/views/imgs/4.png", 200, 200, false, false));
        imageArray.add(new Image("/views/imgs/5.png", 200, 200, false, false));
        imageArray.add(new Image("/views/imgs/6.png", 200, 200, false, false));
        imageArray.add(new Image("/views/imgs/7.png", 200, 200, false, false));
        imageArray.add(new Image("/views/imgs/8.png", 200, 200, false, false));

        rightChar.setGraphic(new ImageView(imageArray.get(2)));
        middleChar.setGraphic(new ImageView(imageArray.get(1)));
        leftChar.setGraphic(new ImageView(imageArray.get(0)));
        leftSel.setGraphic(new ImageView(new Image("/views/imgs/arrowleft.png", 100, 100, false, false)));
        rightSel.setGraphic(new ImageView(new Image("/views/imgs/arrowright.png", 100, 100, false, false)));

        Label Name = new Label("Name: Ellie Williams ");
        Label Class = new Label("Class: Medic ");
        Label MaxHp = new Label("Max Health: 110");
        Label ActionPoints = new Label("Action Points: 6");
        Label Damage = new Label("Attack Damage: 15");
        Label CharSelect = new Label("SELECT YOUR CHARACTER");

        Name.setTranslateY(screenHeight - (screenHeight * 0.8));
        Class.setTranslateY(screenHeight - (screenHeight * 0.75));
        MaxHp.setTranslateY(screenHeight - (screenHeight * 0.70));
        ActionPoints.setTranslateY(screenHeight - (screenHeight * 0.65));
        Damage.setTranslateY(screenHeight - (screenHeight * 0.60));
        CharSelect.setTranslateY(-screenHeight * 0.30);

        root.getChildren().addAll(Name, Class, MaxHp, ActionPoints, Damage, CharSelect);

        ScaleTransition st3 = new ScaleTransition(Duration.millis(30), middleChar);
        st3.setToX(1.25);
        st3.setToY(1.25);
        st3.playFromStart();

        leftSel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (index > 1) {
                    updateLabels(Name, Class, MaxHp, ActionPoints, Damage, current, --index);
                    updateImages(index, rightChar, middleChar, leftChar, imageArray);
                } else if (index == 1) {
                    updateLabels(Name, Class, MaxHp, ActionPoints, Damage, current, 0);
                    rightChar.setGraphic(new ImageView(imageArray.get(1)));
                    middleChar.setGraphic(new ImageView(imageArray.get(0)));
                    leftChar.setGraphic(new ImageView(imageArray.get(current.size() - 1)));
                    index--;
                } else if (index == 0) {
                    index = 7;
                    updateLabels(Name, Class, MaxHp, ActionPoints, Damage, current, 7);
                    rightChar.setGraphic(new ImageView(imageArray.get(0)));
                    middleChar.setGraphic(new ImageView(imageArray.get(index)));
                    leftChar.setGraphic(new ImageView(imageArray.get(index - 1)));
                }
            }
        });
        rightSel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (index < 6) {
                    updateImages(++index, rightChar, middleChar, leftChar, imageArray);
                    updateLabels(Name, Class, MaxHp, ActionPoints, Damage, current, index);
                } else if (index == 6) {
                    updateLabels(Name, Class, MaxHp, ActionPoints, Damage, current, 7);
                    rightChar.setGraphic(new ImageView(imageArray.get(0)));
                    middleChar.setGraphic(new ImageView(imageArray.get(7)));
                    leftChar.setGraphic(new ImageView(imageArray.get(6)));
                    index++;
                } else {
                    index = 0;
                    updateLabels(Name, Class, MaxHp, ActionPoints, Damage, current, 0);
                    rightChar.setGraphic(new ImageView(imageArray.get(index + 1)));
                    middleChar.setGraphic(new ImageView(imageArray.get(index)));
                    leftChar.setGraphic(new ImageView(imageArray.get(current.size() - 1)));
                }
            }
        });
        Continue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                switchToGame(primaryStage, current.get(index));
            }
        });
        backToMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                index = 1;
                switchToMainMenu(primaryStage);
            }
        });
        root.setBackground(null);
        primaryStage.show();
    }

    public void switchToGame(Stage primaryStage, Hero h) {
        StackPane root = new StackPane();
        GridPane game = new GridPane();
        game.setMaxSize(screenHeight * 0.9, screenHeight * 0.9);
        game.setPrefSize(screenHeight * 0.9, screenHeight * 0.9);
        root.getChildren().add(game);
        game.setAlignment(Pos.CENTER);

        Game.startGame(h);

        Label selectOverlay = new Label();
        selectOverlay.setGraphic(new ImageView(new Image("file:src/views/imgs/overlay.png", screenHeight * 0.9 / 15,
                screenHeight * 0.9 / 15, false, false)));

        Label selected = new Label();
        selected.setGraphic(new ImageView(new Image("file:src/views/imgs/overlay.png", screenHeight * 0.9 / 15,
                screenHeight * 0.9 / 15, false, false)));

        Label selected1 = new Label();
        selected1.setGraphic(new ImageView(new Image("file:src/views/imgs/overlay.png", screenHeight * 0.9 / 15,
                screenHeight * 0.9 / 15, false, false)));

        Label Ellie = new Label();
        Ellie.setGraphic(new ImageView(new Image("file:src/views/imgs/Ellie2.png", screenHeight * 0.75 * 0.8 / 15,
                screenHeight * 0.75 * 0.8 / 15, false, false)));

        Label Joel = new Label();
        Joel.setGraphic(new ImageView(new Image("file:src/views/imgs/Joel2.png", screenHeight * 0.75 * 0.8 / 15,
                screenHeight * 0.75 * 0.8 / 15, false, false)));

        Label Name = new Label();
        Label Class = new Label();
        Label MaxHp = new Label();
        Label ActionPoints = new Label();
        Label Damage = new Label();

        root.getChildren().addAll(Name, Class, MaxHp, ActionPoints, Damage);

        Name.setTranslateX(screenWidth / 3);
        Class.setTranslateX(screenWidth / 3);
        MaxHp.setTranslateX(screenWidth / 3);
        ActionPoints.setTranslateX(screenWidth / 3);
        Damage.setTranslateX(screenWidth / 3);

        Name.setTranslateY(-screenHeight * 0.1);
        Class.setTranslateY(-screenHeight * 0.05);
        ActionPoints.setTranslateY(screenHeight * 0.05);
        Damage.setTranslateY(screenHeight * 0.1);

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
                Button button = (Button) e.getSource();
                StackPane stackpane = (StackPane) button.getParent();
                int row1 = GridPane.getRowIndex(stackpane);
                int row = 14 - row1;
                int col = GridPane.getColumnIndex(stackpane);
                if (e.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
                    stackpane.getChildren().add(0, selectOverlay);
                } else if (e.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
                    stackpane.getChildren().remove(selectOverlay);
                } else if (e.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        if (Game.map[row][col] instanceof CharacterCell && ((CharacterCell) Game.map[row][col])
                                .getCharacter() != currentHero) {
                            if (stackpane.getChildren().contains(selected1)) {
                                stackpane.getChildren().remove(selected1);
                                System.out.println("Target removed " + currentTarget.getName());
                                currentTarget = null;
                            } else {
                                stackpane.getChildren().add(0, selected1);
                                currentTarget = ((CharacterCell) Game.map[row][col])
                                        .getCharacter();
                                System.out.println("Target selected " + currentTarget.getName());
                            }
                        }
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        if (Game.map[row][col] instanceof CharacterCell
                                && ((CharacterCell) Game.map[row][col]).getCharacter() instanceof Hero) {
                            if (stackpane.getChildren().contains(selected)) {
                                stackpane.getChildren().remove(selected);
                                Name.setText("");
                                Class.setText("");
                                MaxHp.setText("");
                                ActionPoints.setText("");
                                Damage.setText("");
                                currentHero = null;
                            } else {
                                Name.setText("Name: " + ((CharacterCell) Game.map[row][col]).getCharacter().getName());
                                Class.setText("Class: " + ((CharacterCell) Game.map[row][col]).getCharacter().getClass()
                                        .getSimpleName());
                                MaxHp.setText(
                                        "Health: " + ((CharacterCell) Game.map[row][col]).getCharacter().getCurrentHp()
                                                + "/" + ((CharacterCell) Game.map[row][col]).getCharacter().getMaxHp());
                                ActionPoints.setText("Actions Available: "
                                        + ((Hero) ((CharacterCell) Game.map[row][col]).getCharacter())
                                                .getActionsAvailable()
                                        + "/"
                                        + ((Hero) ((CharacterCell) Game.map[row][col]).getCharacter()).getMaxActions());
                                Damage.setText("Attack Damage: "
                                        + ((Hero) ((CharacterCell) Game.map[row][col]).getCharacter()).getAttackDmg());
                                stackpane.getChildren().add(0, selected);
                                currentHero = ((Hero) ((CharacterCell) Game.map[row][col]).getCharacter());
                            }
                        }
                    }

                }
            }
        };
        // initializeGame
        updateGridUI(game, eventHandler);

        EventHandler<KeyEvent> keyboardHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                Direction direction = null;
                switch (e.getCode()) {
                    case W:
                        direction = Direction.UP;
                        break;
                    case A:
                        direction = Direction.LEFT;
                        break;
                    case S:
                        direction = Direction.DOWN;
                        break;
                    case D:
                        direction = Direction.RIGHT;
                        break;
                }
                try {
                    currentHero.move(direction);
                    updateGridUI(game, eventHandler);
                } catch (MovementException movementException) {
                    // TODO Auto-generated catch block
                    System.out.println("Illegal Move");
                } catch (NotEnoughActionsException actionsException) {
                    // TODO Auto-generated catch block
                    System.out.println("Not enough actions");
                } catch (Exception notSelected) {
                    // TODO Auto-generated catch block
                    System.out.println("You have to select a character");
                }
            }
        };

        Game.printBoard();

        Scene scene = primaryStage
                .getScene();
        scene.setOnKeyPressed(keyboardHandler);
        scene.setRoot(root);
        root.setBackground(null);
        primaryStage.show();
    }

    private void updateGridUI(GridPane game, EventHandler<MouseEvent> eventHandler) {
        Label Ellie = new Label();
        Ellie.setGraphic(new ImageView(new Image("file:src/views/imgs/Ellie2.png", screenHeight * 0.75 * 0.8 / 15,
                screenHeight * 0.75 * 0.8 / 15, false, false)));

        Label Joel = new Label();
        Joel.setGraphic(new ImageView(new Image("file:src/views/imgs/Joel2.png", screenHeight * 0.75 * 0.8 / 15,
                screenHeight * 0.75 * 0.8 / 15, false, false)));
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                StackPane stackpane = new StackPane();
                Button button = new Button();
                button.setMaxSize(64, 64);
                button.setMinSize(32, 32);
                button.setPrefSize(64, 64);
                stackpane
                        .setBackground(
                                new Background(new BackgroundImage(
                                        new Image("file:src/views/imgs/default_visible2.png",
                                                screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false, false),
                                        null, null, null, null)));

                if (Game.map[i][j] instanceof CharacterCell
                        && ((CharacterCell) Game.map[i][j]).getCharacter() != null) {
                    if (((CharacterCell) Game.map[i][j]).getCharacter() instanceof Hero) {
                        String name = ((CharacterCell) Game.map[i][j]).getCharacter().getName();
                        switch (name) {
                            case ("Joel Miller"):
                                stackpane.getChildren().add(0, Joel);
                                break;
                            case ("Ellie Williams"):
                                stackpane.getChildren().add(0, Ellie);
                                break;
                            // add rest of characters
                        }
                    } else {
                        Label Zombie = new Label();
                        Zombie.setGraphic(new ImageView(
                                new Image("file:src/views/imgs/zombiephase1.png", 48, 48, false, false)));
                        stackpane.getChildren().add(0, Zombie);
                    }
                }
                if (Game.map[i][j] instanceof CollectibleCell) {
                    if (((CollectibleCell) Game.map[i][j]).getCollectible() instanceof Vaccine) {
                        Label Vaccine = new Label();
                        Vaccine.setGraphic(
                                new ImageView(new Image("file:src/views/imgs/vaccine.png", 48, 48, false, false)));
                        stackpane.getChildren().add(0, Vaccine);
                    } else {
                        Label Supply = new Label();
                        Supply.setGraphic(
                                new ImageView(new Image("file:src/views/imgs/supply.png", 48, 48, false, false)));
                        stackpane.getChildren().add(0, Supply);
                    }
                }
                button.addEventHandler(MouseEvent.MOUSE_ENTERED, eventHandler);
                button.addEventHandler(MouseEvent.MOUSE_EXITED, eventHandler);
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
                stackpane.getChildren().add(button);
                game.add(stackpane, j, 14 - i);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
