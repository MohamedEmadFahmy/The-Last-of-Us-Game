package model.collectibles;

import engine.Game;
// import exceptions.NoAvailableResourcesException;
import model.characters.Hero;

import java.awt.*;
// import model.characters.Zombie;

public class Vaccine implements Collectible {
    public Vaccine() {

    }

    @Override
    public void pickUp(Hero h) {
        h.getVaccineInventory().add(this);
    }

    @Override
    public void use(Hero h) {
        h.getVaccineInventory().remove(this);
        int X = h.getTarget().getLocation().x;
        int Y = h.getTarget().getLocation().y;
        Game.spawnHero(X, Y);
        Game.zombies.remove(h.getTarget());
        Game.updateVisibility(new Point(X,Y));
        h.setTarget(null);
        h.setActionsAvailable(h.getActionsAvailable() - 1);
        // Zombie.ZOMBIES_COUNT -= 1;
    }

}
