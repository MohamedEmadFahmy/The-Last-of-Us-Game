package model.characters;

import java.util.ArrayList;

import model.characters.*;

// import java.lang.annotation.Target;

import exceptions.InvalidTargetException;
import exceptions.NoAvailableResourcesException;
import model.collectibles.Supply;

public class Medic extends Hero {
    public Medic(String Name, int maxHp, int attackDmg, int maxActions) {
        super(Name, maxHp, attackDmg, maxActions);
    }

    @Override
    public void useSpecial() throws InvalidTargetException, NoAvailableResourcesException {
        Character myTarget = getTarget();
        if (!(myTarget instanceof Hero)) {
            throw new InvalidTargetException();
        }
        try {
            Supply supply = getSupplyInventory().get(0);
            supply.use(this);
            if (myTarget.getCurrentHp() == myTarget.getMaxHp()) {
                throw new InvalidTargetException();
            }
            myTarget.setCurrentHp(myTarget.getMaxHp());
        } catch (Exception e) {
            throw new NoAvailableResourcesException();
        }
    }
}
