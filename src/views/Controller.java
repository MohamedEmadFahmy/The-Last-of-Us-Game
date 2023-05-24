package views;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import model.world.Cell;
import javafx.scene.paint.Color;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Controller extends Application {
    static boolean currentSelected;
    static boolean targetSelected;
    static Hero currentHero = null;
    static Character currentTarget = null;
    static int index = 1;
    static Media hover = new Media(new File("src/views/sounds/click.wav").toURI().toString());
    static Media click = new Media(new File("src/views/sounds/mouse_click.wav").toURI().toString());
    static Media main = new Media(new File("src/views/sounds/maintheme.mp3").toURI().toString());
    static Media attackSound = new Media(new File("src/views/sounds/attackSound.mp3").toURI().toString());
    static Media healSound = new Media(new File("src/views/sounds/healSound.mp3").toURI().toString());
    static Media explorerSound = new Media(new File("src/views/sounds/explorerSound.mp3").toURI().toString());
    static Media fighterSound = new Media(new File("src/views/sounds/fighterSound.mp3").toURI().toString());
    static Media deathSound = new Media(new File("src/views/sounds/deathSound.mp3").toURI().toString());
    static boolean playing = false;
    static ArrayList<Hero> current = new ArrayList<Hero>();
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
        Button Continue = new Button("Continue");
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

        Button backToMenu = new Button("Back");
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
        imageArray.add(new Image("/views/imgs/JoelBig.png", 200, 200, false, false));
        imageArray.add(new Image("/views/imgs/EllieBig.png", 200, 200, false, false));
        imageArray.add(new Image("/views/imgs/TessBig.png", 200, 200, false, false));
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
                }
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
        game.setTranslateX(-screenWidth/5);

        Game.startGame(h);

//        Label selectOverlay = new Label();
//        selectOverlay.setGraphic(new ImageView(new Image("file:src/views/imgs/overlay.png", screenHeight * 0.9 / 15,
//                screenHeight * 0.9 / 15, false, false)));
//
//        Label selected = new Label();
//        selected.setGraphic(new ImageView(new Image("file:src/views/imgs/overlay.png", screenHeight * 0.9 / 15,
//                screenHeight * 0.9 / 15, false, false)));
//
//        Label selected1 = new Label();
//        selected1.setGraphic(new ImageView(new Image("file:src/views/imgs/overlay.png", screenHeight * 0.9 / 15,
//                screenHeight * 0.9 / 15, false, false)));

        Label Name = new Label();
        Label Class = new Label();
        Label ActionPoints = new Label();
        Label Damage = new Label();
        Label VaccinesLeft = new Label();
        Label Special = new Label();
        Label SuppliesLeft = new Label();
        Label HeroImg = new Label();
        Label ZombieImg = new Label();
        Label CurrentHp = new Label();

        Rectangle PlayerHpRed = new Rectangle();
        PlayerHpRed.setHeight(35);
        PlayerHpRed.setFill(Color.RED);

        Rectangle PlayerHpGreen =  new Rectangle();
        PlayerHpGreen.setFill(Color.GREEN);
        PlayerHpGreen.setHeight(35);

        Group mainPlayerStats = new Group();
        mainPlayerStats.getChildren().addAll(Name,Class,ActionPoints,Damage,VaccinesLeft,Special,SuppliesLeft);

        Group healthBar = new Group();
        healthBar.getChildren().addAll(PlayerHpRed,PlayerHpGreen,CurrentHp);

        Label ZombieHp = new Label();

        root.getChildren().addAll(mainPlayerStats, HeroImg,ZombieImg,ZombieHp,healthBar);


        mainPlayerStats.setTranslateX(screenWidth / 3);
        healthBar.setTranslateX(screenWidth / 6);
        HeroImg.setTranslateX(screenWidth/6);
        ZombieImg.setTranslateX(screenWidth/6);
        ZombieHp.setTranslateX(screenWidth/6);

        mainPlayerStats.setTranslateY(-screenHeight * 0.35);
        Name.setTranslateY(-screenHeight * 0.44);
        Class.setTranslateY(-screenHeight * 0.41);
        ActionPoints.setTranslateY(-screenHeight * 0.38);
        Damage.setTranslateY(-screenHeight * 0.35);
        VaccinesLeft.setTranslateY(-screenHeight * 0.32);
        Special.setTranslateY(-screenHeight * 0.29);
        SuppliesLeft.setTranslateY(-screenHeight * 0.26);
        HeroImg.setTranslateY(-screenHeight * 0.35);
        ZombieImg.setTranslateY(screenHeight*0.35);
        healthBar.setTranslateY(-screenHeight*0.23);
        ZombieHp.setTranslateY(screenHeight*0.44);




        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
                Button button = (Button) e.getSource();
                StackPane stackpane = (StackPane) button.getParent();
                int row1 = GridPane.getRowIndex(stackpane);
                int row = 14 - row1;
                int col = GridPane.getColumnIndex(stackpane);
                if (e.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
                    // stackpane.getChildren().add(stackpane.getChildren().size()-2, selectOverlay);
                } else if (e.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
                    // stackpane.getChildren().remove(selectOverlay);
                } else if (e.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        currentTarget = ((CharacterCell) Game.map[row][col]).getCharacter();
                        ZombieImg.setGraphic(new ImageView(
                                new Image("file:src/views/imgs/zombiephase1.png", screenHeight*0.2, screenHeight*0.2, false, false)));

                        ZombieHp.setText("Health: " + currentTarget.getCurrentHp() + "/" + currentTarget.getMaxHp());

                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        if (Game.map[row][col] instanceof CharacterCell
                                && ((CharacterCell) Game.map[row][col]).getCharacter() instanceof Hero) {
                            currentHero = ((Hero) ((CharacterCell) Game.map[row][col]).getCharacter());
                            Name.setText("Name: " + currentHero.getName());
                            Class.setText("Class: " + currentHero.getClass().getSimpleName());
                            PlayerHpRed.setWidth(screenHeight*0.2);
                            PlayerHpGreen.setWidth((((double) currentHero.getCurrentHp() / (double) currentHero.getMaxHp()) * screenHeight) * 0.2);
                            CurrentHp.setText(" +" + currentHero.getCurrentHp());
                            ActionPoints.setText("Actions Available: " + currentHero.getActionsAvailable() + "/" + currentHero.getMaxActions());
                            Damage.setText("Attack Damage: " + currentHero.getAttackDmg());
                            VaccinesLeft.setText("Vaccines Left: " + currentHero.getVaccineInventory().size() + " / 5");
                            SuppliesLeft.setText("Supplies Left: " + currentHero.getSupplyInventory().size() + " / 5");
                            Special.setText(("Special: ") + ((Hero) ((CharacterCell) Game.map[row][col]).getCharacter()).isSpecialAction());
                            HeroImg.setGraphic(new ImageView(
                                    new Image("file:src/views/imgs/" + currentHero.getName() + ".png", screenHeight*0.2 ,
                                            screenHeight* 0.2 , false, false)));

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
                            VaccinesLeft.setText("Vaccines Left: "
                                    + ((Hero) currentHero).getVaccineInventory().size() + " / 5");
                            currentTarget = null;
                        } catch (InvalidTargetException ex) {
                            System.out.println("You have to select a valid zombie");
                            System.out.println(currentHero.getLocation());
                            if (currentTarget != null) {
                                System.out.println(currentTarget.getLocation());
                            }
                        } catch (NotEnoughActionsException ex) {
                            System.out.println("Not enough actions");
                        } catch (NoAvailableResourcesException ex) {
                            System.out.println("Not enough vaccines");
                        }
                        break;
                    case Q:
                        if (currentHero instanceof Medic) {
                            try {
                                currentHero.setTarget((currentTarget));
                                currentHero.useSpecial();
                                play(healSound);
                                PlayerHpRed.setWidth(screenHeight*0.2);
                                PlayerHpGreen.setWidth((((double) currentHero.getCurrentHp() / (double) currentHero.getMaxHp()) * screenHeight) * 0.2);
                                CurrentHp.setText(" +" + currentHero.getCurrentHp());
                                SuppliesLeft.setText("Supplies Left: "
                                        + currentHero.getSupplyInventory().size() + " / 5");
                                Special.setText("Special: True");
                                // animation
                                // Special.setText("Special: False");
                            } catch (InvalidTargetException ex) {
                                System.out.println("Target Out of range");
                            } catch (NoAvailableResourcesException ex) {
                                System.out.println("Not enough Supplies");
                            } catch (NullPointerException ex) {
                                System.out.println("You must select a character to heal");
                            }
                        } else {
                            try {
                                currentHero.useSpecial();
                                Special.setText("Special: True");
                                if (currentHero instanceof Explorer) {
                                    updateUI(game);
                                    play(explorerSound);
                                } else {
                                    play(fighterSound);
                                }
                                SuppliesLeft.setText("Supplies Left: "
                                        + ((Hero) currentHero).getSupplyInventory().size() + " / 5");
                            } catch (InvalidTargetException ex) {
                                System.out.println("Target Out of range");
                            } catch (NoAvailableResourcesException ex) {
                                System.out.println("Not enough Supplies");
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
                                ZombieHp.setText("");
                            }
                            else {
                                ZombieImg.setGraphic(new ImageView(
                                        new Image("file:src/views/imgs/zombiephase1.png", screenHeight*0.2, screenHeight*0.2, false, false)));

                                ZombieHp.setText("Health: " + currentTarget.getCurrentHp() + "/" + currentTarget.getMaxHp());
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
                                VaccinesLeft.setText("");
                                Special.setText("");
                                SuppliesLeft.setText("");
                                HeroImg.setGraphic(null);
                                ZombieImg.setGraphic(null);
                                ZombieHp.setText("");
                                currentHero = null;
                                currentTarget = null;
                            } else {
                                PlayerHpRed.setWidth(screenHeight*0.2);
                                PlayerHpGreen.setWidth((((double) currentHero.getCurrentHp() / (double) currentHero.getMaxHp()) * screenHeight) * 0.2);
                                CurrentHp.setText(" +" + currentHero.getCurrentHp());
                                ActionPoints.setText("Actions Available: "
                                        + ((Hero) currentHero)
                                                .getActionsAvailable()
                                        + "/"
                                        + ((Hero) currentHero).getMaxActions());
                            }
                        } catch (InvalidTargetException ex) {
                            System.out.println("Target out of range ");
                            System.out.println(ex.getMessage());
                        } catch (NotEnoughActionsException ex) {
                            System.out.println("Not enough Actions");
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
                        currentHero.move(direction);
                        if (currentHero.getCurrentHp() == 0) {
                            Name.setText("");
                            Class.setText("");
                            PlayerHpRed.setWidth(0);
                            PlayerHpGreen.setWidth(0);
                            CurrentHp.setText("");
                            ActionPoints.setText("");
                            Damage.setText("");
                            VaccinesLeft.setText("");
                            Special.setText("");
                            SuppliesLeft.setText("");
                            currentHero = null;
                            currentTarget = null;
                            StackPane stackpane = (StackPane) game.getChildren().get((x) * 15 + y);
                            stackpane.getChildren().remove(0);
                        } else {
                            PlayerHpRed.setWidth(screenHeight*0.2);
                            PlayerHpGreen.setWidth((((double) currentHero.getCurrentHp() / (double) currentHero.getMaxHp()) * screenHeight) * 0.2);
                            CurrentHp.setText(" +" + currentHero.getCurrentHp());
                            ActionPoints.setText("Actions Available: "
                                    + currentHero.getActionsAvailable()
                                    + "/"
                                    + currentHero.getMaxActions());
                            VaccinesLeft.setText("Vaccines Left: "
                                    + currentHero.getVaccineInventory().size()
                                    + " / 5");
                            SuppliesLeft.setText("Supplies Left: "
                                    + currentHero.getSupplyInventory().size()
                                    + " / 5");
                        }
                        updateMoveUI(currentHero.getLocation().x, currentHero.getLocation().y, x, y, game);
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
                // Game.printBoard();
            }
        };

        Button endGameButton = new Button("End Turn");
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
                    VaccinesLeft.setText("");
                    Special.setText("");
                    SuppliesLeft.setText("");
                    ZombieHp.setText("");
                    ZombieImg.setGraphic(null);
                    HeroImg.setGraphic(null);
                } catch (InvalidTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NotEnoughActionsException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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

        Scene scene = primaryStage
                .getScene();
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
                    stackpane
                            .setBackground(
                                    new Background(new BackgroundImage(
                                            new Image("file:src/views/imgs/default_notvisible.png",
                                                    screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false, false),
                                            null, null, null, null)));
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
                    stackpane
                            .setBackground(
                                    new Background(new BackgroundImage(
                                            new Image("file:src/views/imgs/default_notvisible.png",
                                                    screenHeight * 0.9 / 15, screenHeight * 0.9 / 15, false, false),
                                            null, null, null, null)));
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

    public static void main(String[] args) {
        launch(args);
    }
}
