package model.world;

import engine.Game.*;
import model.characters.*;
import model.world.*;

public abstract class Cell {
    private boolean isVisible;

    public Cell() {
        isVisible = false;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean x) {
        this.isVisible = x;
    }

    public boolean isOccupied() { // used to check if a cell is occupied or not, used in random spawning methods
        if ((this instanceof CharacterCell) && (((CharacterCell) this).getCharacter() != null)) {
            return true;
        }
        if ((this instanceof TrapCell)) {
            return true;
        }
        if ((this instanceof CollectibleCell) && (((CollectibleCell) this).getCollectible() != null)) {
            return true;
        }
        return false;
    }

}
