package model.characters;

import engine.Game;
import exceptions.NoAvailableResourcesException;
import model.collectibles.Supply;

public class Explorer extends Hero {
    public Explorer(String Name, int maxHp, int attackDmg, int maxActions) {
        super(Name, maxHp, attackDmg, maxActions);
    }

    @Override
    public void useSpecial() throws NoAvailableResourcesException {
        try {
            Supply supply = getSupplyInventory().get(0);
            supply.use(this);
        } catch (Exception e) {
            throw new NoAvailableResourcesException("Not enough supply points!");
        }
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Game.map[i][j].setVisible(true);
            }
        }
    }
}
