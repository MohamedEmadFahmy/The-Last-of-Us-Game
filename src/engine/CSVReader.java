package engine;

import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;

import java.io.*;
import java.util.ArrayList;

public class CSVReader {
    public static void loadHeroesCSV(ArrayList<Hero> availableHeroes, String FilePath) throws Exception {
        FileReader file = new FileReader(new File(FilePath));
        BufferedReader reader = new BufferedReader(file);
        String line = reader.readLine();

        while (line != null) {
            String[] content = line.split(",");
            String name = (String) content[0];
            int maxHp = Integer.parseInt(content[2]);
            int maxActions = Integer.parseInt(content[3]);
            int attackDmg = Integer.parseInt(content[4]);
            Hero hero;
            switch (content[1]) {
                case "FIGH":
                    hero = new Fighter(name, maxHp, attackDmg, maxActions);
                    break;
                case "EXP":
                    hero = new Explorer(name, maxHp, attackDmg, maxActions);
                    break;
                default:
                    hero = new Medic(name, maxHp, attackDmg, maxActions);
            }
            availableHeroes.add(hero);
            line = reader.readLine();
        }
        reader.close();
    }
}
