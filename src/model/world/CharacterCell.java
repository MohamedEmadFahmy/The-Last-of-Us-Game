package model.world;
import model.characters.Character;
public class CharacterCell extends Cell{
    private Character character;
    private boolean isSafe;
    public boolean isSafe() {
        return isSafe;
    }
    public void setSafe(boolean x) {
        isSafe = x;
    }
    public Character getCharacter() {
        return character;
    }
    public void setCharacter(Character x) {
        this.character = x;
    }
    public CharacterCell(Character character, boolean safe) {
        this.character = character;
        this.isSafe = safe;

    }
}
