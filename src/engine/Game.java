package engine;

import model.characters.Hero;
import model.characters.Zombie;
import model.world.Cell;

// import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static engine.CSVReader.loadHeroesHelper;

public class Game {
    public static ArrayList<Hero> availableHeroes;
    public static ArrayList<Hero> heros;
    public static ArrayList<Zombie> zombies;
    public static Cell[][] map;

    public static void loadHeroes(String filePath) throws IOException {
            availableHeroes = new ArrayList<Hero>();
            loadHeroesHelper(availableHeroes, filePath);
    }
    public static void main(String[] args)  {
        try {
            loadHeroes("src\\CSV files\\Heros.csv");
        }
        catch(IOException e) {
            System.out.println("Error while parsing file.");
        }
    }
}
