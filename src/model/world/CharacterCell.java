package model.world;

public class CharacterCell extends Cell{
    private Character character;
    private boolean isSafe;
    public boolean isSafe() {
        return isSafe;
    }
    public void setSafe(boolean x) {
        this.isSafe = x;
    }
    public Character getCharacter() {
        return character;
    }
    public void setCharacter(Character x) {
        this.character = x;
    }
    public CharacterCell() {

    }
}
