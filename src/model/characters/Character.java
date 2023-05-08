package model.characters;

import java.awt.*;
import java.util.ArrayList;
import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;
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
        int myI = this.getLocation().y;
        int myJ = this.getLocation().x;

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if ((i <= myI + 1 && i >= myI - 1) && (j <= myJ + 1 && j >= myJ - 1) && (myI != i || myJ != j)) {
                    if (Game.map[j][i] instanceof CharacterCell
                            && ((CharacterCell) Game.map[j][i]).getCharacter() != null) {
                        list.add(((CharacterCell) Game.map[j][i]).getCharacter());
                    }
                }
            }
        }
        return list;
    }

    public abstract void attack() throws InvalidTargetException, NotEnoughActionsException;

    public void defend(Character c) {
        int attackValue = this.getAttackDmg() / 2;
        int targetHP = c.getCurrentHp();
        int newTargetHP = targetHP - attackValue;
        if (newTargetHP > 0) {
            c.setCurrentHp(newTargetHP);
            return;
        }
        c.onCharacterDeath();
    }

    public void onCharacterDeath() {

        int x = this.getLocation().x;
        int y = this.getLocation().y;

        CharacterCell currentCell = (CharacterCell) Game.map[y][x];
        currentCell.setCharacter(null);
        if (this instanceof Zombie) {
            Game.spawnZombie();
            Game.zombies.remove(this);
            return;
        }
        Game.heroes.remove(this);
    }
}
