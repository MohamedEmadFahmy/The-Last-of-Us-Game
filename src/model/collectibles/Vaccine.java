package model.collectibles;

import exceptions.NoAvailableResourcesException;
import model.characters.Hero;

public class Vaccine implements Collectible{
    public Vaccine() {

    }
    @Override
    public void pickup(Hero h) {
        h.getVaccineInventory().add(new Vaccine());
    }
    @Override
    public void use(Hero h) throws NoAvailableResourcesException {
        try {
            h.getVaccineInventory().remove(0);
        }
        catch (Exception e) {
            throw new NoAvailableResourcesException();
        }
    }
}
