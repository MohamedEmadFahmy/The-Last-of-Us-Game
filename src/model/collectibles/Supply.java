package model.collectibles;

// import exceptions.NoAvailableResourcesException;
import model.characters.Hero;

public class Supply implements Collectible {
    public Supply() {

    }

    @Override
    public void pickup(Hero h) {
        h.getSupplyInventory().add(this);
    }

    @Override
    public void use(Hero h) {
        h.getSupplyInventory().remove(this);
    }
}
