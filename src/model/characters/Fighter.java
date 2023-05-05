package model.characters;

import exceptions.NoAvailableResourcesException;
import model.collectibles.Supply;

public class Fighter extends Hero {
    public Fighter(String Name, int maxHp, int attackDmg, int maxActions) {
        super(Name, maxHp, attackDmg, maxActions);
    }

    @Override
    public void useSpecial() throws NoAvailableResourcesException {
        try {
            Supply supply = getSupplyInventory().get(0);
            supply.use(this);
        } catch (Exception e) {
            throw new NoAvailableResourcesException();
        }
        if (isSpecialAction()) {
            System.out.println("Already in Use");
            return;
        }
        setSpecialAction(true);
    }

}
