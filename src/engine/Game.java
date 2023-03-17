package engine;

import model.characters.Hero;
import model.characters.Zombie;
import model.world.Cell;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static engine.CSVReader.loadHeroesHelper;

public class Game {
    public static ArrayList<Hero> availableHeros;
    public static ArrayList<Hero> heros;
    public static ArrayList<Zombie> zombies;
    public static Cell[][] map;

    public static void loadHeros(String filePath) throws FileNotFoundException {
        loadHeroesHelper(availableHeros,filePath);
    }

}
