package model.world;

import model.characters.Character;

public class CharacterCell extends Cell {

    private Character character;
    private boolean isSafe;

    public CharacterCell(Character character) {
        this.character = character;
        this.isSafe = false;
    }

    public CharacterCell(Character character, boolean isSafe) {
        this.character = character;
        this.isSafe = isSafe;
    }

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

}
