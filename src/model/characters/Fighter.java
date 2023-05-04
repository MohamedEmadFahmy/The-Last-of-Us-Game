package model.characters;

import exceptions.InvalidTargetException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;

public class Fighter extends Hero {
    public Fighter(String Name, int maxHp, int attackDmg, int maxActions) {
        super(Name, maxHp, attackDmg, maxActions);
    }

    @Override
    public void useSpecial()  {
        if(!this.isSpecialAction()){
            System.out.println("Already in Use");
            return;
        }
        setSpecialAction(true);
    }

}
