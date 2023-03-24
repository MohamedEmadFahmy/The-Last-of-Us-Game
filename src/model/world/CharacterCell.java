package model.world;

import model.characters.Character;
import model.characters.*;

public class CharacterCell extends Cell {

    private Character character;
    private boolean isSafe;

    public boolean isSafe() {
        return isSafe;
    }

    public void setSafe(boolean x) {
        isSafe = x;
    }

    public Character getCharacter() {
        return this.character;
    }

    public void setCharacter(Character x) {
        this.character = x;
    }

    public CharacterCell(Character character) { // feeh 7ewar
        // super();
        this.character = character;
    }

    public CharacterCell(Character character, boolean isSafe) { // feeh 7ewar
        // super();
        this.character = character;
        this.isSafe = isSafe;
    }

    public static void main(String[] args) {
        CharacterCell c = new CharacterCell(null, true);
        CharacterCell d = new CharacterCell(null);
        d.setVisible(true);
        // c.isVisible;
        System.out.println(c.isVisible());
        System.out.println(d.isVisible());
        System.out.println(c instanceof Cell);
    }
}
