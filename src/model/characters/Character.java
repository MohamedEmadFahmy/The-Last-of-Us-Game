package model.characters;

import java.awt.*;

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
    public Character() {

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
        this.currentHp = x;
    }
    public int getAttackDmg() {
        return this.attackDmg;
    }
    public Character getTarget(){
        return this.target;
    }
    public void setTarget(Character x) {
        this.target = x;
    }
    public static void main(String[] args) {
        // Character c = new Character();
    }
}
