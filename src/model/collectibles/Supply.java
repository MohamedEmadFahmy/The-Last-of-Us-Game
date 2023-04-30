package model.collectibles;

import exceptions.NoAvailableResourcesException;
import model.characters.Hero;

public class Supply implements Collectible {
    public Supply() {

    }

    @Override
    public void pickup(Hero h) {
        h.getSupplyInventory().add(new Supply());
    }

    @Override
    public void use(Hero h) throws NoAvailableResourcesException {
        try {
            h.getSupplyInventory().remove(0);
        }
        catch (Exception e) {
            throw new NoAvailableResourcesException();
        }
    }
}
