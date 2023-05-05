package model.collectibles;

// import exceptions.NoAvailableResourcesException;
import model.characters.Hero;

public class Vaccine implements Collectible {
    public Vaccine() {

    }

    @Override
    public void pickup(Hero h) {
        h.getVaccineInventory().add(this);
    }

    @Override
    public void use(Hero h) {
        h.getVaccineInventory().remove(this);
    }
}
