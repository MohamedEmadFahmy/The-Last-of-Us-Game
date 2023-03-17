package engine;
import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
public class CSVReader {
    public static void loadHeroesHelper(ArrayList<Hero> availableHeroes, String FilePath) throws IOException {
        Scanner file = new Scanner(new File(FilePath));
        Scanner sc = new Scanner("");
        String[] current = new String[5];
        while (file.hasNext()) {
            String s = file.nextLine();
            sc = new Scanner(s);
            sc.useDelimiter(",");
            for (int i = 0; sc.hasNext(); i++) {
                current[i] = sc.next();
            }
            if (current[1].equalsIgnoreCase("Figh")){
                availableHeroes.add(new Fighter(current[0],Integer.parseInt(current[2]), Integer.parseInt(current[4]),Integer.parseInt(current[3])));
            }
            else if (current[1].equalsIgnoreCase("Med")) {
                availableHeroes.add(new Medic(current[0],Integer.parseInt(current[2]), Integer.parseInt(current[4]),Integer.parseInt(current[3])));
            }
            else  {
                availableHeroes.add(new Explorer(current[0],Integer.parseInt(current[2]), Integer.parseInt(current[4]),Integer.parseInt(current[3])));
            }

        }
        //Class to put CSV reading method to make code look easier to read
    }
}
