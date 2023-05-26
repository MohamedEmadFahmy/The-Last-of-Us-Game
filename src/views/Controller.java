package views;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.characters.*;
import model.characters.Character;
import model.collectibles.Vaccine;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;
import model.world.Cell;
import javafx.scene.paint.Color;
import java.io.File;
import java.util.ArrayList;

import static engine.Game.heroes;

public class Controller extends Application {
    static Hero currentHero;
    static Character currentTarget;
    static int index;
    static Media hover = new Media(new File("src/views/sounds/click.wav").toURI().toString());
    static Media click = new Media(new File("src/views/sounds/mouse_click.wav").toURI().toString());
    static Media main = new Media(new File("src/views/sounds/maintheme.mp3").toURI().toString());
    static Media attackSound = new Media(new File("src/views/sounds/attackSound.mp3").toURI().toString());
    static Media healSound = new Media(new File("src/views/sounds/healSound.mp3").toURI().toString());
    static Media explorerSound = new Media(new File("src/views/sounds/explorerSound.mp3").toURI().toString());
    static Media fighterSound = new Media(new File("src/views/sounds/fighterSound.mp3").toURI().toString());
    static Media deathSound = new Media(new File("src/views/sounds/deathSound.mp3").toURI().toString());
    static Media trapSound = new Media(new File("src/views/sounds/trapSound.mp3").toURI().toString());
    static boolean playing = false;
    static ArrayList<Hero> current;
    static double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
    static double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    static Background notvisible = new Background(new BackgroundImage(
            new Image("file:src/views/imgs/default_notvisible.png",
                    screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false, false),
            null, null, null, null));
    static Background visible = new Background(new BackgroundImage(
            new Image("file:src/views/imgs/default_visible.png",
                    screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false, false),
            null, null, null, null));
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

    public void updateLabels(Label Name, Label Class, Label Hp, Label ActionPoints, Label Damage,
            ArrayList<Hero> current, int index) {
        Name.setText("Name: " + current.get(index).getName());
        Class.setText("Class: " + current.get(index).getClass().getSimpleName());
        Hp.setText("Max Health: " + current.get(index).getMaxHp());
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
        // to disable auto selecting buttons in menu
        root.addEventFilter(KeyEvent.ANY, Event::consume);
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
        index = 1;
        try {
            if (Game.availableHeroes.isEmpty()) {
                Game.loadHeroes("src/CSV files/Heros.csv");
            }
        } catch (Exception e) {
            System.out.println("Couldnt Load Heroes");
            e.printStackTrace();
        }
        current = new ArrayList<Hero>();
        for (int i = 0; i < Game.availableHeroes.size(); i++) {
            current.add(Game.availableHeroes.get(i));
        }
        StackPane root = new StackPane();
        Scene scene = primaryStage.getScene();
        scene.setRoot(root);
        ImagePattern pattern2 = new ImagePattern(new Image("/views/imgs/characterSelect.jpg"));
        scene.setFill(pattern2);
        Button leftSel = new Button();
        leftSel.setMaxSize(200, 200);
        Label rightChar = new Label();
        Label leftChar = new Label();
        Label middleChar = new Label();
        Button rightSel = new Button();
        Button Continue = new Button("CONTINUE");
        ScaleTransition stC = new ScaleTransition(Duration.millis(30), Continue);
        Continue.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Continue.getStyleClass().add("hover");
                stC.setToX(1.05);
                stC.setToY(1.05);
                stC.playFromStart();
                play(hover);
            }
        });

        Continue.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Continue.getStyleClass().remove("hover");
                stC.setToX(1);
                stC.setToY(1);
                stC.playFromStart();
            }
        });

        Button backToMenu = new Button("BACK");
        ScaleTransition stB = new ScaleTransition(Duration.millis(30), backToMenu);
        backToMenu.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                backToMenu.getStyleClass().add("hover");
                stB.setToX(1.05);
                stB.setToY(1.05);
                stB.playFromStart();
                play(hover);
            }
        });

        backToMenu.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                backToMenu.getStyleClass().remove("hover");
                stB.setToX(1);
                stB.setToY(1);
                stB.playFromStart();
            }
        });

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
        imageArray.add(new Image("/views/imgs/Joel Miller.png", 200, 200, false, false));
        imageArray.add(new Image("/views/imgs/Ellie Williams.png", 200, 200, false, false));
        imageArray.add(new Image("/views/imgs/Tess.png", 200, 200, false, false));
        imageArray.add(new Image("/views/imgs/Riley Abel.png", 200, 200, false, false));
        imageArray.add(new Image("/views/imgs/Tommy Miller.png", 200, 200, false, false));
        imageArray.add(new Image("/views/imgs/Bill.png", 200, 200, false, false));
        imageArray.add(new Image("/views/imgs/David.png", 200, 200, false, false));
        imageArray.add(new Image("/views/imgs/Henry Burell.png", 200, 200, false, false));

        rightChar.setGraphic(new ImageView(imageArray.get(2)));
        middleChar.setGraphic(new ImageView(imageArray.get(1)));
        leftChar.setGraphic(new ImageView(imageArray.get(0)));
        leftSel.setGraphic(new ImageView(new Image("/views/imgs/arrowleft.png", 100, 100, false, false)));
        rightSel.setGraphic(new ImageView(new Image("/views/imgs/arrowright.png", 100, 100, false, false)));

        ScaleTransition stRight = new ScaleTransition(Duration.millis(30), rightSel);
        rightSel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                rightSel.setGraphic(
                        new ImageView(new Image("/views/imgs/arrowright_hover.png", 100, 100, false, false)));
                ;
                stRight.setToX(1.05);
                stRight.setToY(1.05);
                stRight.playFromStart();
                play(hover);
            }
        });

        rightSel.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                rightSel.setGraphic(new ImageView(new Image("/views/imgs/arrowright.png", 100, 100, false, false)));

                stRight.setToX(1);
                stRight.setToY(1);
                stRight.playFromStart();
            }
        });

        ScaleTransition stLeft = new ScaleTransition(Duration.millis(30), leftSel);
        leftSel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                leftSel.setGraphic(
                        new ImageView(new Image("/views/imgs/arrowleft_hover.png", 100, 100, false, false)));
                ;
                stLeft.setToX(1.05);
                stLeft.setToY(1.05);
                stLeft.playFromStart();
                play(hover);
            }
        });

        leftSel.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                leftSel.setGraphic(new ImageView(new Image("/views/imgs/arrowleft.png", 100, 100, false, false)));
                stLeft.setToX(1);
                stLeft.setToY(1);
                stLeft.playFromStart();
            }
        });

        Label Name = new Label("Name: Ellie Williams ");
        Label Class = new Label("Class: Medic ");
        Label Hp = new Label("Max Health: 110");
        Label ActionPoints = new Label("Action Points: 6");
        Label Damage = new Label("Attack Damage: 15");
        Label CharSelect = new Label("SELECT YOUR CHARACTER");

        Name.setTranslateY(screenHeight - (screenHeight * 0.8));
        Class.setTranslateY(screenHeight - (screenHeight * 0.75));
        Hp.setTranslateY(screenHeight - (screenHeight * 0.70));
        ActionPoints.setTranslateY(screenHeight - (screenHeight * 0.65));
        Damage.setTranslateY(screenHeight - (screenHeight * 0.60));
        CharSelect.setTranslateY(-screenHeight * 0.30);

        root.getChildren().addAll(Name, Class, Hp, ActionPoints, Damage, CharSelect);

        ScaleTransition st3 = new ScaleTransition(Duration.millis(30), middleChar);
        st3.setToX(1.25);
        st3.setToY(1.25);
        st3.playFromStart();

        leftSel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (index > 1) {
                    updateLabels(Name, Class, Hp, ActionPoints, Damage, current, --index);
                    updateImages(index, rightChar, middleChar, leftChar, imageArray);
                } else if (index == 1) {
                    updateLabels(Name, Class, Hp, ActionPoints, Damage, current, 0);
                    rightChar.setGraphic(new ImageView(imageArray.get(1)));
                    middleChar.setGraphic(new ImageView(imageArray.get(0)));
                    leftChar.setGraphic(new ImageView(imageArray.get(current.size() - 1)));
                    index--;
                } else if (index == 0) {
                    index = 7;
                    updateLabels(Name, Class, Hp, ActionPoints, Damage, current, 7);
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
                    updateLabels(Name, Class, Hp, ActionPoints, Damage, current, index);
                } else if (index == 6) {
                    updateLabels(Name, Class, Hp, ActionPoints, Damage, current, 7);
                    rightChar.setGraphic(new ImageView(imageArray.get(0)));
                    middleChar.setGraphic(new ImageView(imageArray.get(7)));
                    leftChar.setGraphic(new ImageView(imageArray.get(6)));
                    index++;
                } else {
                    index = 0;
                    updateLabels(Name, Class, Hp, ActionPoints, Damage, current, 0);
                    rightChar.setGraphic(new ImageView(imageArray.get(index + 1)));
                    middleChar.setGraphic(new ImageView(imageArray.get(index)));
                    leftChar.setGraphic(new ImageView(imageArray.get(current.size() - 1)));
                }
            }
        });
        Continue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                switchToTutorial(primaryStage, current.get(index));
            }
        });
        backToMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                index = 1;
                switchToMainMenu(primaryStage);
            }
        });

        root.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode keyCode = keyEvent.getCode();
                switch (keyCode) {
                    case LEFT, A:
                        leftSel.fire();
                        break;
                    case RIGHT, D:
                        rightSel.fire();
                        break;
                    case ENTER:
                        Continue.fire();
                        break;
                    case ESCAPE:
                        backToMenu.fire();
                        break;
                    default:
                        break;
                }
            }
        });
        root.setBackground(null);
        primaryStage.show();
    }

    public void switchToTutorial(Stage primaryStage, Hero h) {
        StackPane root = new StackPane();
        Scene scene = primaryStage.getScene();
        scene.setRoot(root);
        ImagePattern pattern2 = new ImagePattern(new Image("/views/imgs/howtoplay.png"));
        scene.setFill(pattern2);
        Button Continue = new Button("CONTINUE");
        root.getChildren().add(Continue);
        Continue.setTranslateY(screenHeight * 0.40);
        Continue.setTranslateX(screenWidth * 0.40);
        ScaleTransition stC = new ScaleTransition(Duration.millis(30), Continue);
        Continue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                switchToGame(primaryStage, h);
            }
        });
        Continue.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Continue.getStyleClass().add("hover");
                stC.setToX(1.05);
                stC.setToY(1.05);
                stC.playFromStart();
                play(hover);
            }
        });

        Continue.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Continue.getStyleClass().remove("hover");
                stC.setToX(1);
                stC.setToY(1);
                stC.playFromStart();
            }
        });
        root.setBackground(null);
        primaryStage.show();
    }

    public void switchToGame(Stage primaryStage, Hero h) {
        currentHero = null;
        currentTarget = null;
        StackPane root = new StackPane();
        GridPane game = new GridPane();
        game.setMaxSize(screenHeight * 0.9, screenHeight * 0.9);
        game.setPrefSize(screenHeight * 0.9, screenHeight * 0.9);

        Scene scene = primaryStage.getScene();
        ImagePattern pattern2 = new ImagePattern(new Image("/views/imgs/characterSelect.jpg"));
        scene.setFill(pattern2);

        Game.startGame(h);
        Game.printBoard();

        // labels for Zombie + Hero
        Label Name = new Label();
        Label Class = new Label();
        Label ActionPoints = new Label();
        Label Damage = new Label();
        HBox VaccinesLeft = new HBox();
        HBox SuppliesLeft = new HBox();
        Label HeroImg = new Label();
        Label ZombieImg = new Label();
        Label CurrentHp = new Label();
        Label characterOverlay = new Label();
        characterOverlay.setGraphic(new ImageView(
                new Image("file:src/views/imgs/characterOverlay.png", ((screenWidth) / 1920) * 750,
                        ((screenWidth) / 1920) * 320, false, false)));
        Label remChars = new Label();
        remChars.setGraphic(new ImageView(
                new Image("file:src/views/imgs/characterOverlay.png", ((screenWidth) / 1920) * 750,
                        ((screenWidth) / 1920) * 320, false, false)));
        Label target = new Label();
        target.setGraphic(new ImageView(
                new Image("file:src/views/imgs/target.png", ((screenWidth) / 1920) * 300,
                        ((screenWidth) / 1920) * 300, false, false)));
        // initialize remaining heroes stuff
        HBox Heroes = new HBox(20);

        for (int i = 0; i < 5; i++) {
            VBox Hero = new VBox();
            Label subImg = new Label();
            subImg.setId("remChars");

            Rectangle heroHpRed = new Rectangle();
            heroHpRed.setHeight(14);
            heroHpRed.setFill(Color.RED);

            Rectangle heroHpGreen = new Rectangle();
            heroHpGreen.setFill(Color.GREEN);
            heroHpGreen.setHeight(14);
            Label subHp = new Label();
            subHp.setId("remChars");

            Group healthBar = new Group();
            healthBar.getChildren().addAll(heroHpRed, heroHpGreen, subHp);

            Label subName = new Label();
            subName.setId("remChars");
            Label subDamage = new Label();
            subDamage.setId("remChars");
            Label subActions = new Label();
            subActions.setId("remChars");
            Label subClass = new Label();
            subClass.setId("remChars");

            Hero.getChildren().addAll(subImg, healthBar, subName, subDamage, subActions, subClass);
            Heroes.getChildren().add(Hero);
        }

        // Player Health Bar
        Rectangle PlayerHpRed = new Rectangle();
        PlayerHpRed.setHeight(35);
        PlayerHpRed.setFill(Color.RED);

        Rectangle PlayerHpGreen = new Rectangle();
        PlayerHpGreen.setFill(Color.GREEN);
        PlayerHpGreen.setHeight(35);

        // Zombie Health Bar
        Rectangle zombieHpRed = new Rectangle();
        zombieHpRed.setHeight(35);
        zombieHpRed.setFill(Color.RED);

        Rectangle zombieHpGreen = new Rectangle();
        zombieHpGreen.setFill(Color.GREEN);
        zombieHpGreen.setHeight(35);

        Label ZombieHp = new Label();

        // group for main player stats
        Group mainPlayerStats = new Group();
        mainPlayerStats.getChildren().addAll(Name, Class, ActionPoints, Damage, VaccinesLeft, SuppliesLeft);

        // group for health bar for main player
        Group healthBar = new Group();
        healthBar.getChildren().addAll(PlayerHpRed, PlayerHpGreen, CurrentHp);

        // group for health bar for Zombie
        Group zombieHealthBar = new Group();
        zombieHealthBar.getChildren().addAll(zombieHpRed, zombieHpGreen, ZombieHp);

        // adding all groups to the root stackpane
        root.getChildren().addAll(characterOverlay, remChars, target, mainPlayerStats, HeroImg, ZombieImg,
                zombieHealthBar, healthBar,
                Heroes);
        root.getChildren().add(game);
        game.setAlignment(Pos.CENTER);
        game.setTranslateX(-screenWidth / 5);

        // alignment things
        mainPlayerStats.setTranslateX(screenWidth / 3);
        characterOverlay.setTranslateX(screenWidth / 3.6);
        healthBar.setTranslateX(screenWidth / 6);
        HeroImg.setTranslateX(screenWidth / 6);
        ZombieImg.setTranslateX(screenWidth / 6);
        zombieHealthBar.setTranslateX(screenWidth / 6);
        Heroes.setTranslateX(screenWidth * 0.594);
        remChars.setTranslateX(screenWidth / 3.6);
        target.setTranslateX(screenWidth / 5.75);

        target.setTranslateY(screenHeight * 0.34);
        Heroes.setTranslateY(screenHeight * 0.43);
        characterOverlay.setTranslateY(-screenHeight * 0.32);
        mainPlayerStats.setTranslateY(-screenHeight * 0.33);
        Name.setTranslateY(-screenHeight * 0.44);
        Class.setTranslateY(-screenHeight * 0.41);
        ActionPoints.setTranslateY(-screenHeight * 0.38);
        Damage.setTranslateY(-screenHeight * 0.35);
        VaccinesLeft.setTranslateY(-screenHeight * 0.32);
        SuppliesLeft.setTranslateY(-screenHeight * 0.27);
        HeroImg.setTranslateY(-screenHeight * 0.35);
        ZombieImg.setTranslateY(screenHeight * 0.32);
        healthBar.setTranslateY(-screenHeight * 0.23);
        zombieHealthBar.setTranslateY(screenHeight * 0.44);

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
                Button button = (Button) e.getSource();
                StackPane stackpane = (StackPane) button.getParent();
                int row1 = GridPane.getRowIndex(stackpane);
                int row = 14 - row1;
                int col = GridPane.getColumnIndex(stackpane);
                if (e.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
                    ((Button) stackpane.getChildren().get(stackpane.getChildren().size() - 1)).setGraphic(
                            new ImageView(
                                    new Image("file:src/views/imgs/overlay.png", screenHeight * 0.9 / 15,
                                            screenHeight * 0.9 / 15, false, false)));
                } else if (e.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
                    ((Button) stackpane.getChildren().get(stackpane.getChildren().size() - 1)).setGraphic(null);
                } else if (e.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        if (Game.map[row][col] instanceof CharacterCell
                                && ((CharacterCell) Game.map[row][col]).getCharacter() != null) {
                            currentTarget = ((CharacterCell) Game.map[row][col]).getCharacter();
                            if (currentTarget instanceof Zombie) {
                                ZombieImg.setGraphic(new ImageView(
                                        new Image("file:src/views/imgs/zombiephase1.png", screenHeight * 0.2,
                                                screenHeight * 0.2, false, false)));

                                zombieHpRed.setWidth(screenHeight * 0.2);
                                zombieHpGreen.setWidth(
                                        (((double) currentTarget.getCurrentHp() / (double) currentTarget.getMaxHp())
                                                * screenHeight) * 0.2);
                                ZombieHp.setText(" +" + currentTarget.getCurrentHp());
                            } else {
                                ZombieImg.setGraphic(new ImageView(
                                        new Image("file:src/views/imgs/" + currentTarget.getName() + ".png",
                                                screenHeight * 0.2,
                                                screenHeight * 0.2, false, false)));

                                zombieHpRed.setWidth(screenHeight * 0.2);
                                zombieHpGreen.setWidth(
                                        (((double) currentTarget.getCurrentHp() / (double) currentTarget.getMaxHp())
                                                * screenHeight) * 0.2);
                                ZombieHp.setText(" +" + currentTarget.getCurrentHp());
                            }
                            updateRemainingHeroes(Heroes);
                        } else {
                            currentTarget = null;
                            ZombieImg.setGraphic(null);

                            zombieHpRed.setWidth(0);
                            zombieHpGreen.setWidth(0);
                            ZombieHp.setText("");
                        }
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        if (Game.map[row][col] instanceof CharacterCell
                                && ((CharacterCell) Game.map[row][col]).getCharacter() instanceof Hero) {
                            currentHero = ((Hero) ((CharacterCell) Game.map[row][col]).getCharacter());
                            Name.setText("Name: " + currentHero.getName());
                            Class.setText("Class: " + currentHero.getClass().getSimpleName());
                            PlayerHpRed.setWidth(screenHeight * 0.2);
                            PlayerHpGreen
                                    .setWidth((((double) currentHero.getCurrentHp() / (double) currentHero.getMaxHp())
                                            * screenHeight) * 0.2);
                            CurrentHp.setText(" +" + currentHero.getCurrentHp());
                            ActionPoints.setText("Actions Available: " + currentHero.getActionsAvailable() + "/"
                                    + currentHero.getMaxActions());
                            Damage.setText("Attack Damage: " + currentHero.getAttackDmg());
                            HeroImg.setGraphic(new ImageView(
                                    new Image("file:src/views/imgs/" + currentHero.getName() + ".png",
                                            screenHeight * 0.2,
                                            screenHeight * 0.2, false, false)));

                            updateRemainingHeroes(Heroes);
                            updateSupplyVaccine(SuppliesLeft, VaccinesLeft);
                        }
                    }

                }
            }
        };
        // initializeGame
        loadGrid(game, eventHandler);

        EventHandler<KeyEvent> keyboardHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (currentHero == null) {
                    System.out.println("You have to select a hero to do an action");
                    return;
                }
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
                    case V:

                        try {
                            currentHero.setTarget(currentTarget);
                            currentHero.cure();
                            StackPane prev = (StackPane) game.getChildren()
                                    .get((currentTarget.getLocation().x) * 15 + currentTarget.getLocation().y);
                            prev.getChildren().remove(0);
                            updateMoveUI(currentHero.getLocation().x, currentHero.getLocation().y,
                                    currentHero.getLocation().x, currentHero.getLocation().y, game);
                            System.out.println(
                                    "Cure @ " + currentHero.getLocation() + " & " + currentTarget.getLocation());
                            updateSupplyVaccine(SuppliesLeft, VaccinesLeft);
                            currentTarget = null;
                            ZombieImg.setGraphic(null);
                            zombieHpRed.setWidth(0);
                            zombieHpGreen.setWidth(0);
                            ZombieHp.setText("");
                        } catch (InvalidTargetException ex) {
                            System.out.println("You have to select a valid zombie");
                            // System.out.println(currentHero.getLocation());
                            // if (currentTarget != null) {
                            // System.out.println(currentTarget.getLocation());
                            // }
                            displayAlert(root, game, currentHero.getLocation().x, currentHero.getLocation().y,
                                    ex.getMessage());
                        } catch (NotEnoughActionsException ex) {
                            System.out.println("Not enough actions");
                            displayAlert(root, game, currentHero.getLocation().x, currentHero.getLocation().y,
                                    ex.getMessage());
                        } catch (NoAvailableResourcesException ex) {
                            System.out.println("Not enough vaccines");
                            displayAlert(root, game, currentHero.getLocation().x, currentHero.getLocation().y,
                                    ex.getMessage());
                        }
                        break;
                    case Q:
                        if (currentHero instanceof Medic) {
                            try {
                                currentHero.setTarget((currentTarget));
                                currentHero.useSpecial();
                                play(healSound);
                                PlayerHpRed.setWidth(screenHeight * 0.2);
                                PlayerHpGreen.setWidth(
                                        (((double) currentHero.getCurrentHp() / (double) currentHero.getMaxHp())
                                                * screenHeight) * 0.2);
                                CurrentHp.setText(" +" + currentHero.getCurrentHp());
                                updateSupplyVaccine(SuppliesLeft, VaccinesLeft);
                                ZombieImg.setGraphic(new ImageView(
                                        new Image("file:src/views/imgs/" + currentTarget.getName() + ".png",
                                                screenHeight * 0.2,
                                                screenHeight * 0.2, false, false)));

                                zombieHpRed.setWidth(screenHeight * 0.2);
                                zombieHpGreen.setWidth(
                                        (((double) currentTarget.getCurrentHp() / (double) currentTarget.getMaxHp())
                                                * screenHeight) * 0.2);
                                ZombieHp.setText(" +" + currentTarget.getCurrentHp());
                            } catch (InvalidTargetException ex) {
                                System.out.println("Target Out of range");
                                displayAlert(root, game, currentHero.getLocation().x, currentHero.getLocation().y,
                                        ex.getMessage());
                            } catch (NoAvailableResourcesException ex) {
                                System.out.println("Not enough Supplies");
                                displayAlert(root, game, currentHero.getLocation().x, currentHero.getLocation().y,
                                        ex.getMessage());
                            } catch (NullPointerException ex) {
                                System.out.println("You must select a character to heal");
                                displayAlert(root, game, currentHero.getLocation().x, currentHero.getLocation().y,
                                        ex.getMessage());
                            }
                        } else {
                            try {
                                currentHero.useSpecial();
                                if (currentHero instanceof Explorer) {
                                    updateUI(game);
                                    play(explorerSound);
                                } else {
                                    play(fighterSound);
                                }
                                updateSupplyVaccine(SuppliesLeft, VaccinesLeft);
                            } catch (InvalidTargetException ex) {
                                System.out.println("Target Out of range");
                                displayAlert(root, game, currentHero.getLocation().x, currentHero.getLocation().y,
                                        ex.getMessage());
                            } catch (NoAvailableResourcesException ex) {
                                System.out.println("Not enough Supplies");
                                displayAlert(root, game, currentHero.getLocation().x, currentHero.getLocation().y,
                                        ex.getMessage());
                            }
                        }
                        break;
                    case E:
                        currentHero.setTarget(currentTarget);
                        try {
                            currentHero.attack();
                            play(attackSound);
                            updateMoveUI(currentHero.getLocation().x,
                                    currentHero.getLocation().y, currentHero.getLocation().x,
                                    currentHero.getLocation().y, game);
                            if (currentTarget.getCurrentHp() == 0) {
                                currentTarget = null;
                                Zombie spawnedZombie = Game.zombies.get(Game.zombies.size() - 1);
                                int x = spawnedZombie.getLocation().x;
                                int y = spawnedZombie.getLocation().y;
                                System.out.println(x + " " + y);
                                StackPane stackpane = (StackPane) game.getChildren().get((x) * 15 + y);
                                if (((Cell) Game.map[x][y]).isVisible()) {
                                    Label Zombie = new Label();
                                    Zombie.setGraphic(
                                            new ImageView(new Image("file:src/views/imgs/zombiephase1.png", 48, 48,
                                                    false, false)));
                                    stackpane.getChildren().add(0, Zombie);
                                }
                                ZombieImg.setGraphic(null);
                                zombieHpRed.setWidth(0);
                                zombieHpGreen.setWidth(0);
                                ZombieHp.setText("");
                            } else {
                                ZombieImg.setGraphic(new ImageView(
                                        new Image("file:src/views/imgs/zombiephase1.png", screenHeight * 0.2,
                                                screenHeight * 0.2, false, false)));

                                zombieHpRed.setWidth(screenHeight * 0.2);
                                zombieHpGreen.setWidth(
                                        (((double) currentTarget.getCurrentHp() / (double) currentTarget.getMaxHp())
                                                * screenHeight) * 0.2);
                                ZombieHp.setText(" +" + currentTarget.getCurrentHp());
                            }
                            if (currentHero.getCurrentHp() == 0) {
                                play(deathSound);
                                Name.setText("");
                                Class.setText("");
                                PlayerHpRed.setWidth(0);
                                PlayerHpGreen.setWidth(0);
                                CurrentHp.setText("");
                                ActionPoints.setText("");
                                Damage.setText("");
                                VaccinesLeft.getChildren().clear();
                                SuppliesLeft.getChildren().clear();
                                HeroImg.setGraphic(null);
                                ZombieImg.setGraphic(null);
                                ZombieHp.setText("");
                                zombieHpRed.setWidth(0);
                                zombieHpGreen.setWidth(0);
                                currentHero = null;
                                currentTarget = null;
                            } else {
                                PlayerHpRed.setWidth(screenHeight * 0.2);
                                PlayerHpGreen.setWidth(
                                        (((double) currentHero.getCurrentHp() / (double) currentHero.getMaxHp())
                                                * screenHeight) * 0.2);
                                CurrentHp.setText(" +" + currentHero.getCurrentHp());
                                ActionPoints.setText("Actions Available: "
                                        + ((Hero) currentHero)
                                                .getActionsAvailable()
                                        + "/"
                                        + ((Hero) currentHero).getMaxActions());
                            }
                        } catch (InvalidTargetException ex) {
                            System.out.println("Target out of range ");
                            displayAlert(root, game, currentHero.getLocation().x, currentHero.getLocation().y,
                                    ex.getMessage());
                        } catch (NotEnoughActionsException ex) {
                            System.out.println("Not enough Actions");
                            displayAlert(root, game, currentHero.getLocation().x, currentHero.getLocation().y,
                                    ex.getMessage());
                        } catch (NullPointerException ex) {
                            System.out.println("No target is currently selected");
                        }
                        break;
                    default:
                        break;

                }
                if (direction != null) {
                    try {
                        int x = currentHero.getLocation().x;
                        int y = currentHero.getLocation().y;

                        // handle moving into a trapcell
                        boolean isTrap = false;
                        int newX = x;
                        int newY = y;
                        switch (direction) {
                            case UP:
                                newX++;
                                break;
                            case DOWN:
                                newX--;
                                break;
                            case LEFT:
                                newY--;
                                break;
                            case RIGHT:
                                newY++;
                                break;
                        }
                        if ((newX <= 14 && newX >= 0) && (newY <= 14 && newY >= 0)
                                && Game.map[newX][newY] instanceof TrapCell) {
                            isTrap = true;
                        }
                        currentHero.move(direction);
                        if (currentHero.getCurrentHp() == 0) {
                            Name.setText("");
                            Class.setText("");
                            PlayerHpRed.setWidth(0);
                            PlayerHpGreen.setWidth(0);
                            CurrentHp.setText("");
                            ActionPoints.setText("");
                            Damage.setText("");
                            VaccinesLeft.getChildren().clear();
                            SuppliesLeft.getChildren().clear();
                            currentHero = null;
                            currentTarget = null;
                            StackPane stackpane = (StackPane) game.getChildren().get((x) * 15 + y);
                            stackpane.getChildren().remove(0);
                        } else {
                            PlayerHpRed.setWidth(screenHeight * 0.2);
                            PlayerHpGreen
                                    .setWidth((((double) currentHero.getCurrentHp() / (double) currentHero.getMaxHp())
                                            * screenHeight) * 0.2);
                            CurrentHp.setText(" +" + currentHero.getCurrentHp());
                            ActionPoints.setText("Actions Available: "
                                    + currentHero.getActionsAvailable()
                                    + "/"
                                    + currentHero.getMaxActions());
                            updateSupplyVaccine(SuppliesLeft, VaccinesLeft);
                        }
                        updateMoveUI(currentHero.getLocation().x, currentHero.getLocation().y, x, y, game);
                        if (isTrap) {
                            StackPane stackpane = (StackPane) game.getChildren().get((newX) * 15 + newY);
                            ImageView trap = new ImageView(
                                    new Image("file:src/views/imgs/trap.png", (screenHeight * 0.9 / 15) * (0.6 / 0.64),
                                            (screenHeight * 0.9 / 15) * (0.6 / 0.64), false, false));

                            stackpane.getChildren().add(stackpane.getChildren().size() - 2, trap);
                            PauseTransition wait = new PauseTransition(Duration.seconds(1));
                            wait.setOnFinished((e2) -> trap.setVisible(false));
                            wait.play();
                            play(trapSound);

                        }
                    } catch (MovementException movementException) {
                        // TODO Auto-generated catch block
                        System.out.println("Illegal Move");
                        displayAlert(root, game, currentHero.getLocation().x, currentHero.getLocation().y,
                                movementException.getMessage());
                        // System.out.println("line 934");
                    } catch (NotEnoughActionsException actionsException) {
                        // TODO Auto-generated catch block
                        System.out.println("Not enough actions");
                        displayAlert(root, game, currentHero.getLocation().x, currentHero.getLocation().y,
                                actionsException.getMessage());
                    } catch (NullPointerException notSelected) {
                        // TODO Auto-generated catch block
                        System.out.println("You have to select a character");
                    }
                }
                updateRemainingHeroes(Heroes);
                if (Game.checkGameOver()) {
                    PauseTransition wait = new PauseTransition(Duration.seconds(3));
                    wait.setOnFinished((pauseEvent) -> {
                        switchToGameEnd(primaryStage);
                        Game.availableHeroes = new ArrayList<Hero>();
                        Game.heroes = new ArrayList<Hero>();
                        Game.zombies = new ArrayList<Zombie>();
                        Game.map = new Cell[15][15];
                    });
                    wait.play();
                }
            }
        };

        Button endGameButton = new Button("END TURN");
        endGameButton.setTranslateX(screenWidth / 2.5);
        endGameButton.setTranslateY(screenHeight * 0.4);
        endGameButton.setId("endTurn");
        endGameButton.addEventFilter(KeyEvent.ANY, Event::consume);
        endGameButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                // TODO Auto-generated method stub
                try {
                    Game.endTurn();
                    updateUI(game);
                    currentHero = null;
                    currentTarget = null;
                    Name.setText("");
                    Class.setText("");
                    PlayerHpRed.setWidth(0);
                    PlayerHpGreen.setWidth(0);
                    CurrentHp.setText("");
                    ActionPoints.setText("");
                    Damage.setText("");
                    VaccinesLeft.getChildren().clear();
                    SuppliesLeft.getChildren().clear();
                    ZombieHp.setText("");
                    zombieHpRed.setWidth(0);
                    zombieHpGreen.setWidth(0);
                    ZombieImg.setGraphic(null);
                    HeroImg.setGraphic(null);
                } catch (InvalidTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NotEnoughActionsException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (Game.checkGameOver()) {
                    PauseTransition wait = new PauseTransition(Duration.seconds(3));
                    wait.setOnFinished((pauseEvent) -> {
                        switchToGameEnd(primaryStage);
                        Game.availableHeroes = new ArrayList<Hero>();
                        Game.heroes = new ArrayList<Hero>();
                        Game.zombies = new ArrayList<Zombie>();
                        Game.map = new Cell[15][15];
                    });
                    wait.play();
                }
            }
        });

        ScaleTransition stEndGame = new ScaleTransition(Duration.millis(30), endGameButton);
        endGameButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                endGameButton.getStyleClass().add("hover");
                stEndGame.setToX(1.05);
                stEndGame.setToY(1.05);
                stEndGame.playFromStart();
                play(hover);
            }
        });

        endGameButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                endGameButton.getStyleClass().remove("hover");
                stEndGame.setToX(1);
                stEndGame.setToY(1);
                stEndGame.playFromStart();
            }
        });
        root.getChildren().add(endGameButton);

        root.setOnKeyPressed(keyboardHandler);
        scene.setRoot(root);
        root.setBackground(null);
        primaryStage.show();
    }

    private void loadGrid(GridPane game, EventHandler<MouseEvent> eventHandler) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                StackPane stackpane = new StackPane();
                Button button = new Button();
                button.setMaxSize(64, 64);
                button.setMinSize(32, 32);
                button.setPrefSize(64, 64);
                if (!(Game.map[i][j].isVisible())) {
                    int random = ((int) (Math.random() * 15));
                    switch (random) {
                        case 1, 2, 3, 4, 5, 6, 7, 8, 9, 14:
                            stackpane
                                    .setBackground(
                                            new Background(new BackgroundImage(
                                                    new Image("file:src/views/imgs/invisible main.png",
                                                            screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false,
                                                            false),
                                                    null, null, null, null)));
                            break;
                        case 10:
                            stackpane
                                    .setBackground(
                                            new Background(new BackgroundImage(
                                                    new Image("file:src/views/imgs/invisible var1.png",
                                                            screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false,
                                                            false),
                                                    null, null, null, null)));
                            break;
                        case 0:
                            stackpane
                                    .setBackground(
                                            new Background(new BackgroundImage(
                                                    new Image("file:src/views/imgs/invisible var2.png",
                                                            screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false,
                                                            false),
                                                    null, null, null, null)));
                            break;
                        case 11:
                            stackpane
                                    .setBackground(
                                            new Background(new BackgroundImage(
                                                    new Image("file:src/views/imgs/invisible var3.png",
                                                            screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false,
                                                            false),
                                                    null, null, null, null)));
                            break;
                        case 12:
                            stackpane
                                    .setBackground(
                                            new Background(new BackgroundImage(
                                                    new Image("file:src/views/imgs/invisible var4.png",
                                                            screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false,
                                                            false),
                                                    null, null, null, null)));
                            break;
                        case 13:
                            stackpane
                                    .setBackground(
                                            new Background(new BackgroundImage(
                                                    new Image("file:src/views/imgs/invisible var5.png",
                                                            screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false,
                                                            false),
                                                    null, null, null, null)));
                            break;
                    }

                } else {
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
                            Label Hero = new Label();
                            Hero.setGraphic(new ImageView(
                                    new Image("file:src/views/imgs/" + name + ".png", screenHeight * 0.75 * 0.8 / 15,
                                            screenHeight * 0.75 * 0.8 / 15, false, false)));
                            stackpane.getChildren().add(0, Hero);
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
                }
                button.addEventHandler(MouseEvent.MOUSE_ENTERED, eventHandler);
                button.addEventHandler(MouseEvent.MOUSE_EXITED, eventHandler);
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
                stackpane.getChildren().add(button);
                game.add(stackpane, j, 14 - i);
                // -----
            }
        }
    }

    private void updateMoveUI(int x, int y, int oldx, int oldy, GridPane gridPane) {
        StackPane prev = (StackPane) gridPane.getChildren().get((oldx) * 15 + oldy);
        prev.getChildren().remove(0);
        for (int i = Math.max(0, x - 1); i <= Math.min(14, x + 1); i++) {
            for (int j = Math.max(0, y - 1); j <= Math.min(14, y + 1); j++) {
                StackPane stackpane = (StackPane) gridPane.getChildren().get((i) * 15 + j);
                if (stackpane.getChildren().size() > 1) {
                    // System.out.println(stackpane.getChildren().size());
                    stackpane.getChildren().remove(0);
                }
                stackpane
                        .setBackground(
                                new Background(
                                        new BackgroundImage(
                                                new Image("file:src/views/imgs/default_visible2.png",
                                                        screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false, false),
                                                null, null, null, null)));
                if (Game.map[i][j] instanceof CharacterCell
                        && ((CharacterCell) Game.map[i][j]).getCharacter() != null) {
                    if (((CharacterCell) Game.map[i][j]).getCharacter() instanceof Hero) {
                        String name = ((CharacterCell) Game.map[i][j]).getCharacter().getName();
                        Label Hero = new Label();
                        Hero.setGraphic(new ImageView(
                                new Image("file:src/views/imgs/" + name + ".png", screenHeight * 0.75 * 0.8 / 15,
                                        screenHeight * 0.75 * 0.8 / 15, false, false)));
                        stackpane.getChildren().add(0, Hero);
                    } else {
                        Label Zombie = new Label();
                        Zombie.setGraphic(
                                new ImageView(new Image("file:src/views/imgs/zombiephase1.png", 48, 48, false, false)));
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
            }
        }
    }

    private void updateUI(GridPane gridPane) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                StackPane stackpane = (StackPane) gridPane.getChildren().get((i) * 15 + j);
                if (stackpane.getChildren().size() > 1) {
                    stackpane.getChildren().remove(0);
                }
                if (!(Game.map[i][j].isVisible())) {
                    int random = ((int) (Math.random() * 15));
                    switch (random) {
                        case 1, 2, 3, 4, 5, 6, 7, 8, 9, 14:
                            stackpane
                                    .setBackground(
                                            new Background(new BackgroundImage(
                                                    new Image("file:src/views/imgs/invisible main.png",
                                                            screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false,
                                                            false),
                                                    null, null, null, null)));
                            break;
                        case 10:
                            stackpane
                                    .setBackground(
                                            new Background(new BackgroundImage(
                                                    new Image("file:src/views/imgs/invisible var1.png",
                                                            screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false,
                                                            false),
                                                    null, null, null, null)));
                            break;
                        case 0:
                            stackpane
                                    .setBackground(
                                            new Background(new BackgroundImage(
                                                    new Image("file:src/views/imgs/invisible var2.png",
                                                            screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false,
                                                            false),
                                                    null, null, null, null)));
                            break;
                        case 11:
                            stackpane
                                    .setBackground(
                                            new Background(new BackgroundImage(
                                                    new Image("file:src/views/imgs/invisible var3.png",
                                                            screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false,
                                                            false),
                                                    null, null, null, null)));
                            break;
                        case 12:
                            stackpane
                                    .setBackground(
                                            new Background(new BackgroundImage(
                                                    new Image("file:src/views/imgs/invisible var4.png",
                                                            screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false,
                                                            false),
                                                    null, null, null, null)));
                            break;
                        case 13:
                            stackpane
                                    .setBackground(
                                            new Background(new BackgroundImage(
                                                    new Image("file:src/views/imgs/invisible var5.png",
                                                            screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false,
                                                            false),
                                                    null, null, null, null)));
                            break;
                    }
                } else {
                    stackpane
                            .setBackground(
                                    new Background(
                                            new BackgroundImage(
                                                    new Image("file:src/views/imgs/default_visible2.png",
                                                            screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false,
                                                            false),
                                                    null, null, null, null)));
                    if (Game.map[i][j] instanceof CharacterCell
                            && ((CharacterCell) Game.map[i][j]).getCharacter() != null) {
                        if (((CharacterCell) Game.map[i][j]).getCharacter() instanceof Hero) {
                            String name = ((CharacterCell) Game.map[i][j]).getCharacter().getName();
                            Label Hero = new Label();
                            Hero.setGraphic(new ImageView(
                                    new Image("file:src/views/imgs/" + name + ".png", screenHeight * 0.75 * 0.8 / 15,
                                            screenHeight * 0.75 * 0.8 / 15, false, false)));
                            stackpane.getChildren().add(0, Hero);
                        } else {
                            Label Zombie = new Label();
                            Zombie.setGraphic(
                                    new ImageView(
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
                }
            }
        }
    }

    private void updateRemainingHeroes(HBox Heroes) {
        ArrayList<Hero> curr = new ArrayList<>();
        for (int i = 0; i < heroes.size(); i++) {
            curr.add(heroes.get(i));
        }
        curr.remove(currentHero);
        for (int i = 0; i < 5; i++) {
            if (i < curr.size()) {
                VBox currentVBox = (VBox) Heroes.getChildren().get(i);
                ((Label) currentVBox.getChildren().get(0)).setGraphic(new ImageView(
                        new Image("file:src/views/imgs/" + curr.get(i).getName() + ".png", screenHeight * 0.1,
                                screenHeight * 0.1, false, false)));

                ((Rectangle) ((Group) currentVBox.getChildren().get(1)).getChildren().get(0))
                        .setWidth(screenHeight * 0.1);
                ((Rectangle) ((Group) currentVBox.getChildren().get(1)).getChildren().get(1))
                        .setWidth((screenHeight * 0.1)
                                * ((double) curr.get(i).getCurrentHp() / (double) curr.get(i).getMaxHp()));
                ((Label) ((Group) currentVBox.getChildren().get(1)).getChildren().get(2))
                        .setText(" + " + curr.get(i).getCurrentHp());
                ((Label) currentVBox.getChildren().get(2)).setText("Name: " + curr.get(i).getName());
                ((Label) currentVBox.getChildren().get(3)).setText("Damage: " + curr.get(i).getAttackDmg());
                ((Label) currentVBox.getChildren().get(4))
                        .setText("Actions: " + curr.get(i).getActionsAvailable() + "/" + curr.get(i).getMaxActions());
                ((Label) currentVBox.getChildren().get(5)).setText("Class: " + curr.get(i).getClass().getSimpleName());
            } else {
                VBox currentVBox = (VBox) Heroes.getChildren().get(i);
                ((Label) currentVBox.getChildren().get(0)).setGraphic(null);
                ((Rectangle) ((Group) currentVBox.getChildren().get(1)).getChildren().get(0)).setWidth(0);
                ((Rectangle) ((Group) currentVBox.getChildren().get(1)).getChildren().get(1)).setWidth(0);
                ((Label) ((Group) currentVBox.getChildren().get(1)).getChildren().get(2)).setText("");
                ((Label) currentVBox.getChildren().get(2)).setText(null);
                ((Label) currentVBox.getChildren().get(3)).setText(null);
                ((Label) currentVBox.getChildren().get(4)).setText(null);
                ((Label) currentVBox.getChildren().get(5)).setText(null);

            }
        }
    }

    public void switchToGameEnd(Stage primaryStage) {
        // primaryStage.initStyle(StageStyle.TRANSPARENT);
        BorderPane root = new BorderPane();
        Scene scene = primaryStage.getScene();
        scene.setRoot(root);
        String mainMenuCSS = this.getClass().getResource("/views/styles/mainMenu.css").toExternalForm();
        scene.getStylesheets().add(mainMenuCSS);
        if (Game.checkWin()) {
            ImagePattern pattern = new ImagePattern(new Image("/views/imgs/victoryScreen.png"));
            scene.setFill(pattern);
        } else {
            ImagePattern pattern2 = new ImagePattern(new Image("/views/imgs/defeatScreen.jpg"));
            scene.setFill(pattern2);
        }
        Button newGameBtn = new Button("NEW GAME");
        root.addEventFilter(KeyEvent.ANY, Event::consume);
        ScaleTransition st = new ScaleTransition(Duration.millis(30), newGameBtn);
        st.setCycleCount(1);
        st.setInterpolator(Interpolator.EASE_BOTH);
        if (playing == false) {
            play(main);
            playing = true;
        }

        newGameBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                newGameBtn.getStyleClass().add("hover");
                st.setToX(1.05);
                st.setToY(1.05);
                st.playFromStart();
                play(hover);
            }
        });

        newGameBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                newGameBtn.getStyleClass().remove("hover");
                st.setToX(1);
                st.setToY(1);
                st.playFromStart();
            }
        });
        newGameBtn.setOnAction(new EventHandler<ActionEvent>() {
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

        newGameBtn.setTranslateY(-screenHeight / 3.5);
        newGameBtn.setTranslateX(screenWidth / 25);
        root.setBottom(newGameBtn);

        Button quitToMenue = new Button("QUIT GAME");
        ScaleTransition st2 = new ScaleTransition(Duration.millis(30), quitToMenue);
        quitToMenue.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                quitToMenue.getStyleClass().add("redhover");
                st2.setToX(1.05);
                st2.setToY(1.05);
                st2.playFromStart();
                play(hover);
            }
        });

        quitToMenue.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                quitToMenue.getStyleClass().remove("redhover");
                st2.setToX(1);
                st2.setToY(1);
                st2.playFromStart();
            }
        });
        quitToMenue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                try {
                    primaryStage.close();
                } catch (Exception e) {
                    System.out.print("something went wrong.");
                }
            }
        });

        quitToMenue.setTranslateY(screenHeight - (screenHeight / 5));
        quitToMenue.setTranslateX(screenWidth / 27);
        root.setTop(quitToMenue);

        root.setBackground(null);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void displayAlert(StackPane root, GridPane game, int x, int y, String message) {

        StackPane messageBox = new StackPane();

        Text messageText = new Text("");
        messageText.setId("messageText");

        messageText.setTranslateX(20);
        messageText.setTranslateY(20);
        messageText.setWrappingWidth(260);
        messageText.setFill(Color.RED);
        ImageView messageImage = new ImageView(new Image("file:src/views/imgs/characterOverlay2.png",
                300, 128, false, false));
        messageBox.getChildren().addAll(messageImage, messageText);

        StackPane currentStackPane = (StackPane) game.getChildren().get((x) * 15 + y);
        Bounds boundsInScreen = currentStackPane.localToScreen(currentStackPane.getBoundsInLocal());

        System.out.println(boundsInScreen.getMinX());
        System.out.println(boundsInScreen.getMinY());

        int shift = (int) (90 * (screenWidth / 1920));

        double paneMinX;
        double paneMinY;
        if (boundsInScreen.getMinX() > (screenWidth / 1920) * 607) {
            paneMinX = (boundsInScreen.getMinX() - (screenWidth / 1920) * (607 - 172)) + shift;
            paneMinY = boundsInScreen.getMinY() - shift;
        } else {
            paneMinX = boundsInScreen.getMinX() + shift;
            paneMinY = boundsInScreen.getMinY() - shift;
        }

        root.getChildren().add(root.getChildren().size(), messageBox);

        messageBox.setAlignment(Pos.TOP_LEFT);
        messageBox.setTranslateX(paneMinX);
        messageBox.setTranslateY(Math.max(paneMinY, 100));

        ((Text) messageBox.getChildren().get(1)).setText(message);

        PauseTransition wait = new PauseTransition(Duration.seconds(1));
        wait.setOnFinished((pauseEvent) -> {
            root.getChildren().remove(messageBox);
        });
        wait.play();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void updateSupplyVaccine(HBox supply, HBox vaccine) {
        supply.getChildren().clear();
        vaccine.getChildren().clear();

        for (int i = 0; i < currentHero.getSupplyInventory().size(); i++) {
            Label Supply = new Label();
            Supply.setGraphic(
                    new ImageView(new Image("file:src/views/imgs/supply.png", 48, 48, false, false)));
            supply.getChildren().add(Supply);
        }
        for (int j = 0; j < currentHero.getVaccineInventory().size(); j++) {
            Label Supply = new Label();
            Supply.setGraphic(
                    new ImageView(new Image("file:src/views/imgs/vaccine.png", 48, 48, false, false)));
            vaccine.getChildren().add(Supply);
        }
    }
}
