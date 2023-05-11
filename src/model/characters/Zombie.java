package model.characters;

import java.util.ArrayList;

import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;

public class Zombie extends Character {
    static int ZOMBIES_COUNT = 0;

    public Zombie() {
        super("Zombie " + ++ZOMBIES_COUNT, 40, 10);
    }

    @Override
    public void attack() throws InvalidTargetException, NotEnoughActionsException {
        Character target = getAttackPriority();
        setTarget(target);
        if (getTarget() == null) {
            return;
        }
        super.attack();
    }

    private Character getAttackPriority() {
        ArrayList<Character> list = getAdjacentCharacters();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Character current = list.remove(0);
            if (current instanceof Hero) {
                return current;
            }
        }
        return null;
    }

}
