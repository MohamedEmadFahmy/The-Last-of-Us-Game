package engine;

import model.characters.*;
import model.characters.Character;
import model.collectibles.*;
import model.world.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;

import static engine.CSVReader.loadHeroesCSV;

public class Game {
    public static ArrayList<Hero> availableHeroes = new ArrayList<Hero>();
    public static ArrayList<Hero> heroes = new ArrayList<Hero>();
    public static ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    public static Cell[][] map = new Cell[15][15];

    public static void loadHeroes(String filePath) throws Exception {
        loadHeroesCSV(availableHeroes, filePath);
    }

    public static void spawnZombie() { // used when killing a zombie or starting a game
        int X = (int) (Math.random() * 15);
        int Y = (int) (Math.random() * 15);
        while (Game.map[Y][X].isOccupied()) {
            X = (int) (Math.random() * 15);
            Y = (int) (Math.random() * 15);
        }
        Zombie spawnedZombie = new Zombie();
        spawnedZombie.setLocation(new Point(X,Y));
        Game.map[Y][X] = new CharacterCell(spawnedZombie);
    }

    public static void spawnTraps() { // spawns 5 traps at the start of the game
        for (int i = 0; i < 5; i++) {
            int X = (int) (Math.random() * 15);
            int Y = (int) (Math.random() * 15);
            while (Game.map[Y][X].isOccupied()) {
                X = (int) (Math.random() * 15);
                Y = (int) (Math.random() * 15);
            }
            Game.map[Y][X] = new TrapCell();
        }
    }

    public static void spawnVaccines() {
        int X;
        int Y;
        for (int i = 0; i < 5; i++) {
            Vaccine vaccine = new Vaccine();
            X = (int) (Math.random() * 15);
            Y = (int) (Math.random() * 15);
            while (Game.map[Y][X].isOccupied()) {
                X = (int) (Math.random() * 15);
                Y = (int) (Math.random() * 15);
            }

            Game.map[Y][X] = new CollectibleCell(vaccine);
        }
    }

    public static void spawnSupplies() {
        int X;
        int Y;
        for (int i = 0; i < 5; i++) {
            Supply supply = new Supply();
            X = (int) (Math.random() * 15);
            Y = (int) (Math.random() * 15);
            while (Game.map[Y][X].isOccupied()) {
                X = (int) (Math.random() * 15);
                Y = (int) (Math.random() * 15);
            }

            Game.map[Y][X] = new CollectibleCell(supply);
        }
    }

    public static void startGame(Hero h) {
        Game.availableHeroes.remove(h);
        heroes.add(h);
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Game.map[j][i] = new CharacterCell(null);
            }
        }
        Game.map[0][0] = new CharacterCell(h);
        spawnSupplies();
        spawnVaccines();
        spawnTraps();
        for (int i = 0; i < 10; i++) {
            spawnZombie();
        }
    }

    public static void printBoard() {
        for (int i = 14; i >= 0; i--) {
            for (int j = 0; j < 15; j++) {
                Cell currentCell = map[i][j];
                if (currentCell instanceof CharacterCell) {
                    if (((CharacterCell) currentCell).getCharacter() == null) {
                        System.out.print("_ ");
                    } else if (((CharacterCell) currentCell).getCharacter() instanceof Zombie) {
                        System.out.print("Z ");
                    } else {
                        System.out.print((((CharacterCell) currentCell).getCharacter().getClass() +
                                "").charAt(23) + " ");
                    }
                } else if (currentCell instanceof CollectibleCell) {
                    if (((CollectibleCell) currentCell).getCollectible() instanceof Vaccine) {
                        System.out.print("V ");
                    } else if (((CollectibleCell) currentCell).getCollectible() instanceof Supply) {
                        System.out.print("S ");
                    }
                } else if (currentCell instanceof TrapCell) {
                    System.out.print("T ");
                } else {
                    System.out.print("N ");
                }
            }
            System.out.println();
        }
    }

    public static void updateVisibility(Point location) {
        int x = location.x;
        int y = location.y;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (((j == x) || (j == x + 1) || (j == x - 1)) && ((i == y) || (i == y + 1) || (i == y - 1))) {
                    Game.map[i][j].setVisible(true);
                }
            }
        }
    }

    public static boolean checkWin() {
        if (heroes.size() < 5) {
            return false;
        }
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Cell currentCell = Game.map[i][j];
                if (currentCell instanceof CollectibleCell) {
                    if (((CollectibleCell) currentCell).getCollectible() instanceof Vaccine) {
                        return false;
                    }
                }
            }
        }
        for (int i = 0; i < Game.heroes.size(); i++) {
            Hero hero = Game.heroes.get(i);
            ArrayList<Vaccine> vaccineInventory = hero.getVaccineInventory();
            if (vaccineInventory.size() != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkGameOver() {
        return Game.heroes.isEmpty();
    }

    public static void endTurn() {
        // update visibility
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Game.map[i][j].setVisible(false); // set all cells to be non visible initially
            }
        }

        for (int i = 0; i < heroes.size(); i++) {
            Hero hero = heroes.get(i);
            updateVisibility(hero.getLocation());
        }

        // make zombies attack
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Cell currentCell = Game.map[i][j];
                if (currentCell instanceof CharacterCell) {
                    Character character = ((CharacterCell) currentCell).getCharacter();
                    if (character instanceof Zombie) {
                        Zombie zombie = (Zombie) character;
                        zombie.attack();
                    }
                }
            }
        }

        // reset actions available for every hero

        for (int i = 0; i < heroes.size(); i++) {
            Hero hero = heroes.get(i);
            hero.setActionsAvailable(hero.getMaxActions());
        }

        // spawn zombie
        spawnZombie();
    }

    public static void main(String[] args) {

        Medic f = new Medic("Mohamed", 1, 20, 5);
        f.setLocation(new Point(0, 0));

        startGame(f);

        Explorer exp = new Explorer("Mohamed", 1, 20, 5);
        exp.setLocation(new Point(2, 2));
        ((CharacterCell) Game.map[2][2]).setCharacter(exp);
        heroes.add(exp);

        printBoard();

        Scanner sc = new Scanner(System.in);
        do {
            Direction d = null;
            while (d == null) {
                System.out.print("Direction: ");
                String input = sc.nextLine();
                switch (input) {
                    case "w":
                        d = Direction.UP;
                        break;
                    case "s":
                        d = Direction.DOWN;
                        break;
                    case "a":
                        d = Direction.LEFT;
                        break;
                    case "d":
                        d = Direction.RIGHT;
                        break;
                }
            }

            if (heroes.get(0).getCurrentHp() <= 0) {
                System.out.println("DEAD");
            }
            try {
                heroes.get(0).move(d);
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Invalid Movement");
                // break;
            }
            System.out.println();
            System.out.println("-------------------");
            System.out.println();
            printBoard();
            System.out.println(heroes);
        } while (!heroes.isEmpty());
        sc.close();
    }
}
