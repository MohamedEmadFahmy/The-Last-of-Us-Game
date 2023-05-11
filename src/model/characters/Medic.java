package model.characters;

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
        if (!(myTarget instanceof Hero) || !this.isValidTarget()) {
            throw new InvalidTargetException();
        }
        try {
            Supply supply = getSupplyInventory().get(0);
            supply.use(this);
        } catch (Exception e) {
            throw new NoAvailableResourcesException();
        }
        myTarget.setCurrentHp(myTarget.getMaxHp());
    }
}
