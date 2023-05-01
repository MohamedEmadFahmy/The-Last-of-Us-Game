package model.characters;

import java.awt.*;
import java.util.ArrayList;
import engine.Game;
import model.world.*;

public abstract class Character {
    private String name;
    private Point location;
    private int maxHp;
    private int currentHp;
    private int attackDmg;
    private Character target;

    public Character(String name, int maxHp, int attackDmg) {
        this.name = name;
        this.maxHp = maxHp;
        this.attackDmg = attackDmg;
        this.currentHp = maxHp;
    }

    public String getName() {
        return this.name;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point x) {
        this.location = x;
    }

    public int getMaxHp() {
        return this.maxHp;
    }

    public int getCurrentHp() {
        return this.currentHp;
    }

    public void setCurrentHp(int x) {
        if (x <= 0) {
            currentHp = 0;

        } else if (x > maxHp)
            currentHp = maxHp;
        else {
            currentHp = x;
        }
    }

    public int getAttackDmg() {
        return this.attackDmg;
    }

    public Character getTarget() {
        return this.target;
    }

    public void setTarget(Character x) {
        this.target = x;
    }

    public ArrayList<Character> getAdjacentCharacters() {
        ArrayList<Character> list = new ArrayList<Character>();
        int myI = 0;
        int myJ = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (Game.map[i][j] instanceof CharacterCell) {
                    if (((CharacterCell) Game.map[i][j]).getCharacter() == this) {
                        myI = i;
                        myJ = j;
                    }
                }
            }
        }
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if ((i <= myI + 1 && i >= myI - 1) && (j <= myJ + 1 && j >= myJ - 1) && (myI != i || myJ != j)) {
                    list.add(((CharacterCell) Game.map[i][j]).getCharacter());
                }
            }
        }
        return list;
    }

    protected Character getAttackPriority() {
        ArrayList<Character> list = getAdjacentCharacters();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Character current = list.remove(0);
            if (current != null) {
                if ((current instanceof Zombie && this instanceof Hero)
                        || (current instanceof Hero && this instanceof Zombie)) {
                    return current;
                }
            }
        }
        return null;
    }

    public void attack() {

    }

}
