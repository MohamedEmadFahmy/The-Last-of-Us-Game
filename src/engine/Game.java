package engine;

import model.characters.*;
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
        while (Game.map[X][Y].isOccupied()) {
            X = (int) (Math.random() * 15);
            Y = (int) (Math.random() * 15);
        }
        Zombie spawnedZombie = new Zombie();
        zombies.add(spawnedZombie);
        spawnedZombie.setLocation(new Point(X, Y));
        Game.map[X][Y] = new CharacterCell(spawnedZombie);
    }

    public static void spawnTraps() { // spawns 5 traps at the start of the game
        for (int i = 0; i < 5; i++) {
            int X = (int) (Math.random() * 15);
            int Y = (int) (Math.random() * 15);
            while (Game.map[X][Y].isOccupied()) {
                X = (int) (Math.random() * 15);
                Y = (int) (Math.random() * 15);
            }
            Game.map[X][Y] = new TrapCell();
        }
    }

    public static void spawnVaccines() {
        int X;
        int Y;
        for (int i = 0; i < 5; i++) {
            Vaccine vaccine = new Vaccine();
            X = (int) (Math.random() * 15);
            Y = (int) (Math.random() * 15);
            while (Game.map[X][Y].isOccupied()) {
                X = (int) (Math.random() * 15);
                Y = (int) (Math.random() * 15);
            }

            Game.map[X][Y] = new CollectibleCell(vaccine);
        }
    }

    public static void spawnSupplies() {
        int X;
        int Y;
        for (int i = 0; i < 5; i++) {
            Supply supply = new Supply();
            X = (int) (Math.random() * 15);
            Y = (int) (Math.random() * 15);
            while (Game.map[X][Y].isOccupied()) {
                X = (int) (Math.random() * 15);
                Y = (int) (Math.random() * 15);
            }

            Game.map[X][Y] = new CollectibleCell(supply);
        }
    }

    public static void spawnHero(int x, int y) {
        int size = availableHeroes.size();
        int index = (int) (Math.random() * size);
        Hero newHero = availableHeroes.remove(index);

        map[x][y] = new CharacterCell(newHero);
        // ((CharacterCell) map[x][y]).setCharacter(newHero);
        newHero.setLocation(new Point(x, y));
        heroes.add(newHero);
    }

    public static void startGame(Hero h) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Game.map[i][j] = new CharacterCell(null);
            }
        }
        Game.availableHeroes.remove(h);
        heroes.add(h);
        h.setLocation(new Point(0, 0));
        Game.map[0][0] = new CharacterCell(h);
        spawnSupplies();
        spawnVaccines();
        spawnTraps();
        for (int i = 0; i < 10; i++) {
            spawnZombie();
        }
        updateVisibility(new Point(0, 0));
    }

    public static void printBoard() {
        for (int i = 14; i >= 0; i--) {
            for (int j = 0; j < 15; j++) {
                Cell currentCell = map[i][j];
                if (currentCell.isVisible() == false) {
                    System.out.print("X ");
                    continue;
                }
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
                if (((i == x) || (i == x + 1) || (i == x - 1)) && ((j == y) || (j == y + 1) || (j == y - 1))) {
                    map[i][j].setVisible(true);
                }
            }
        }
        map[x][y].setVisible(true);
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
        if (!zombies.isEmpty()) {
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    if (map[i][j] instanceof CollectibleCell) {
                        if (((CollectibleCell) map[i][j]).getCollectible() instanceof Vaccine) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        if (availableHeroes.isEmpty()) {
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    if (map[i][j] instanceof CollectibleCell) {
                        if (((CollectibleCell) map[i][j]).getCollectible() instanceof Vaccine) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        for (int i = 0; i < heroes.size(); i++) {
            if (!(heroes.get(i).getVaccineInventory().isEmpty())) {
                return false;
            }
        }
        if (Game.heroes.isEmpty()) {
            return true;
        }
        boolean VaccineDone = true;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (map[i][j] instanceof CollectibleCell) {
                    if (((CollectibleCell) map[i][j]).getCollectible() instanceof Vaccine) {
                        VaccineDone = false;
                    }
                }
            }
        }
        return VaccineDone;
    }

    public static void endTurn() {
        // update visibility
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                map[i][j].setVisible(false); // set all cells to be non visible initially
            }
        }

        for (int i = 0; i < heroes.size(); i++) {
            Hero hero = heroes.get(i);
            updateVisibility(hero.getLocation());
        }

        // make zombies attack
        for (int i = 0; i < zombies.size(); i++) {
            Zombie curr = zombies.get(i);
            curr.attack();
            curr.setTarget(null);
        }

        // reset actions available for every hero

        for (int i = 0; i < heroes.size(); i++) {
            Hero hero = heroes.get(i);
            hero.setTarget(null);
            hero.setSpecialAction(false);
            hero.setActionsAvailable(hero.getMaxActions());
        }

        // spawn zombie
        spawnZombie();
    }

    public static void main(String[] args) {

        Medic f = new Medic("Mohamed", 1, 999, 6);
        f.setLocation(new Point(0, 0));

        startGame(f);

        Explorer exp = new Explorer("Mohamed", 1, 20, 999);
        exp.setLocation(new Point(2, 2));
        ((CharacterCell) Game.map[2][2]).setCharacter(exp);
        heroes.add(exp);

        printBoard();

        Scanner sc = new Scanner(System.in);
        do {
            Direction d = null;
            if (heroes.get(0).getActionsAvailable() <= 0) {
                endTurn();
                System.out.println("Turn Ended");
                printBoard();
            }
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
                    case "h":
                        // heroes.get(0).attack();
                        break;
                    // case "e":
                    // endTurn();
                    // System.out.println("Turn ended");
                    // break;
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
            // System.out.println(heroes);
            // System.out.println(heroes.get(0).getVaccineInventory());
            System.out.println(heroes.get(0).getAdjacentCharacters());
        } while (!heroes.isEmpty());
        sc.close();
    }
}
