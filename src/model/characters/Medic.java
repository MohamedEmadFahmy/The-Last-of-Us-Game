package model.characters;

// import java.lang.annotation.Target;

import exceptions.InvalidTargetException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import model.collectibles.Supply;

public class Medic extends Hero {
    public Medic(String Name, int maxHp, int attackDmg, int maxActions) {
        super(Name, maxHp, attackDmg, maxActions);
    }

    @Override
    public void useSpecial() throws InvalidTargetException,NotEnoughActionsException,NoAvailableResourcesException {
        this.getSupplyInventory().remove(0);
        Character myTarget = getTarget();
        if(!(myTarget instanceof Hero)){
            throw new InvalidTargetException();
        }
        myTarget.setCurrentHp(myTarget.getMaxHp());
    }


    

}
