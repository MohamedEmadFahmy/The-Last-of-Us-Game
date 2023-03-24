package engine;

import model.characters.Hero;
import model.characters.Zombie;
import model.world.Cell;

// import java.io.FileNotFoundException;
// import java.io.IOException;
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

    // public static void main(String[] args) {
    // try {
    // loadHeroes("src\\CSV files\\Heros.csv");
    // } catch (Exception e) {
    // System.out.println("Error while parsing file.");
    // }
    // }
}
