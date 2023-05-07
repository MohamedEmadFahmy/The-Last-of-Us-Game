package engine;

import model.characters.*;
import model.collectibles.*;
import model.world.*;

import java.util.ArrayList;

import static engine.CSVReader.loadHeroesCSV;

public class Game {
    public static ArrayList<Hero> availableHeroes = new ArrayList<Hero>();
    public static ArrayList<Hero> heroes = new ArrayList<Hero>();
    public static ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    public static Cell[][] map = new Cell[15][15];

    public static void loadHeroes(String filePath) throws Exception {
        loadHeroesCSV(availableHeroes, filePath);
    }

    public void spawnZombie() { // used when killing a zombie or starting a game
        int X = (int) (Math.random() * 15);
        int Y = (int) (Math.random() * 15);
        while (Game.map[Y][X].isOccupied()) {
            X = (int) (Math.random() * 15);
            Y = (int) (Math.random() * 15);
        }
        Zombie spawnedZombie = new Zombie();
        Game.map[Y][X] = new CharacterCell(spawnedZombie);
    }

    public void spawnTraps() { // spawns 5 traps at the start of the game
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

    public void spawnVaccines() {
        Vaccine vaccine = new Vaccine();
        int X;
        int Y;
        for (int i = 0; i < 5; i++) {
            X = (int) (Math.random() * 15);
            Y = (int) (Math.random() * 15);
            while (Game.map[Y][X].isOccupied()) {
                X = (int) (Math.random() * 15);
                Y = (int) (Math.random() * 15);
            }

            Game.map[Y][X] = new CollectibleCell(vaccine);
        }
    }

    public void spawnSupplies() {
        Supply supply = new Supply();
        int X;
        int Y;
        for (int i = 0; i < 5; i++) {
            X = (int) (Math.random() * 15);
            Y = (int) (Math.random() * 15);
            while (Game.map[Y][X].isOccupied()) {
                X = (int) (Math.random() * 15);
                Y = (int) (Math.random() * 15);
            }

            Game.map[Y][X] = new CollectibleCell(supply);
        }
    }

    public void startGame(Hero h) {
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
}
