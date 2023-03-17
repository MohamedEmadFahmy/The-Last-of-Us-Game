package engine;
import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
public class CSVReader {
    public static void loadHeroesHelper(ArrayList<Hero> availableHeroes, String FilePath) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(FilePath));
        sc.useDelimiter(",");
        String[] current = new String[5];
        for(int j = 0; sc.hasNext(); j++) {
            for(int i = 0; i < 5; i ++) {
                current[i] = sc.next();
            }
            if (current[1].equalsIgnoreCase("Fighter")){
                availableHeroes.set(j,new Fighter(current[0],Integer.parseInt(current[2]), Integer.parseInt(current[4]),Integer.parseInt(current[3])));
            }
            else if (current[1].equalsIgnoreCase("Medic")) {
                availableHeroes.set(j,new Medic(current[0],Integer.parseInt(current[2]), Integer.parseInt(current[4]),Integer.parseInt(current[3])));
            }
            else  {
                availableHeroes.set(j,new Explorer(current[0],Integer.parseInt(current[2]), Integer.parseInt(current[4]),Integer.parseInt(current[3])));
            }
        }
        //Class to put CSV reading method to make code look easier to read
    }
}
