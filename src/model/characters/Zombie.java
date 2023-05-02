package model.characters;

import java.util.ArrayList;

public class Zombie extends Character {
    static int ZOMBIES_COUNT = 0;

    public Zombie() {
        super("Zombie " + ++ZOMBIES_COUNT, 40, 10);
    }

    public void attack() {
        Character target = getAttackPriority();
        setTarget(target);
        if (getTarget() == null) {
            return;
        }
        int targetHP = target.getCurrentHp();
        int newTargetHP = targetHP - this.getAttackDmg();
        if (newTargetHP > 0) {
            target.setCurrentHp(newTargetHP);
            target.defend(this);
            return;
        }
        // target.onCharacterDeath();
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
