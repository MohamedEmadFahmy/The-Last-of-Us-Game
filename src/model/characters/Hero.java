package model.characters;

import model.collectibles.Supply;
import model.collectibles.Vaccine;

import java.util.ArrayList;

public class Hero extends Character {
    private int actionsAvailable;
    private int maxActions;
    private boolean specialAction;
    private ArrayList<Vaccine> vaccineInventory;
    private ArrayList<Supply> supplyInventory;
    public Hero(String name, int maxHp, int attackDmg, int maxActions) {
        super(name,maxHp,attackDmg);
        this.maxActions = maxActions;
    }
    public Hero() {

    }
    public int getActionsAvailable() {
        return actionsAvailable;
    }

    public void setActionsAvailable(int x) {
        this.actionsAvailable = x;
    }

    public int getMaxActions() {
        return maxActions;
    }

    public boolean isSpecialAction() {
        return specialAction;
    }

    public void setSpecialAction(boolean x) {
        this.specialAction = x;
    }

    public ArrayList<Vaccine> getVaccineInventory() {
        return vaccineInventory;
    }

    public ArrayList<Supply> getSupplyInventory() {
        return supplyInventory;
    }
}
