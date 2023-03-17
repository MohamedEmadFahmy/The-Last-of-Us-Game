package engine;

import model.characters.Hero;
import model.characters.Zombie;
import model.world.Cell;

// import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static engine.CSVReader.loadHeroesHelper;

public class Game {
    public static ArrayList<Hero> availableHeros;
    public static ArrayList<Hero> heros;
    public static ArrayList<Zombie> zombies;
    public static Cell[][] map;

    public static void loadHeros(String filePath) throws IOException {
            availableHeros = new ArrayList<Hero>();
            loadHeroesHelper(availableHeros, filePath);
    }
    public static void main(String[] args)  {
        try {
            loadHeros("src\\model\\characters\\Heros.csv");
        }
        catch(IOException e) {
            System.out.println("Error while parsing file.");
        }
    }
}
