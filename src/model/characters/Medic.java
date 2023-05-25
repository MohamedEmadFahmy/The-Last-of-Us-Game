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
        if (myTarget == null) {
            throw new InvalidTargetException("Select a hero to heal!");
        }
        if (!(myTarget instanceof Hero)) {
            throw new InvalidTargetException("Cant heal a zombie!");
        }
        if (!this.hasValidTarget()) {
            throw new InvalidTargetException("Target out of range!");
        }
        try {
            Supply supply = getSupplyInventory().get(0);
            supply.use(this);
        } catch (Exception e) {
            throw new NoAvailableResourcesException("Not enough supply points");
        }
        myTarget.setCurrentHp(myTarget.getMaxHp());
    }
}
