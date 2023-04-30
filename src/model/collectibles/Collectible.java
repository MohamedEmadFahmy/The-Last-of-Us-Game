package model.collectibles;

import exceptions.NoAvailableResourcesException;
import model.characters.Hero;

public interface Collectible {
    public void pickup(Hero h) throws NoAvailableResourcesException;
    public void use(Hero h) throws NoAvailableResourcesException;
}
