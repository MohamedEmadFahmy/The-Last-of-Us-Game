package model.characters;

import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NotEnoughActionsException;
import exceptions.NoAvailableResourcesException;
import model.collectibles.*;
import model.world.*;
import engine.*;
import java.awt.*;
import java.util.ArrayList;

import java.util.ArrayList;

public abstract class Hero extends Character {
    private int actionsAvailable;
    private int maxActions;
    private boolean specialAction;
    private ArrayList<Vaccine> vaccineInventory;
    private ArrayList<Supply> supplyInventory;

    public Hero(String name, int maxHp, int attackDmg, int maxActions) {
        super(name, maxHp, attackDmg);
        this.maxActions = maxActions;
        this.actionsAvailable = maxActions;
        this.supplyInventory = new ArrayList<Supply>();
        this.vaccineInventory = new ArrayList<Vaccine>();
        this.specialAction = false;
    }

    public int getActionsAvailable() {
        return actionsAvailable;
    }

    public void setActionsAvailable(int x) {
        this.actionsAvailable = x;
    }

    public int getMaxActions() {
        return maxActions;
    }

    public boolean isSpecialAction() {
        return specialAction;
    }

    public void setSpecialAction(boolean x) {
        this.specialAction = x;
    }

    public ArrayList<Vaccine> getVaccineInventory() {
        return vaccineInventory;
    }

    public ArrayList<Supply> getSupplyInventory() {
        return supplyInventory;
    }

    public boolean isValidTarget() {
        ArrayList<Character> adjacentCharacters = getAdjacentCharacters();
        return adjacentCharacters.contains(this.getTarget());
    }

    public void attack() throws InvalidTargetException, NotEnoughActionsException {
        if (!(getTarget() instanceof Zombie) || !isValidTarget()) {
            throw new InvalidTargetException();
        }
        if (!(this instanceof Fighter && isSpecialAction())) {
            if (actionsAvailable <= 0) {
                throw new NotEnoughActionsException();
            }
            setActionsAvailable(getActionsAvailable() - 1);
        }
        Character myTarget = getTarget();
        int TargetHP = myTarget.getCurrentHp();
        int NewHP = TargetHP - getAttackDmg();
        myTarget.defend(this);
        if (NewHP > 0) {
            myTarget.setCurrentHp(NewHP);
            return;
        }
        myTarget.onCharacterDeath();
    }

    public abstract void useSpecial() throws InvalidTargetException, NoAvailableResourcesException;

    public boolean checkCharachterCell(Cell targetCell) {
        Character character = ((CharacterCell) targetCell).getCharacter();
        return character != null;
    }

    public void move(Direction D) throws MovementException { // checks if the cell the player is trying to move to is
                                                             // available/empty
        int X = (int) this.getLocation().getX();
        int Y = (int) this.getLocation().getY();
        Cell targetCell;
        if (D == Direction.UP) {
            if (Y + 1 > 14) {
                throw new MovementException();
            }
            Y += 1;
            targetCell = Game.map[Y][X];
        } else if (D == Direction.LEFT) {
            if (X - 1 < 0) {
                throw new MovementException();
            }
            X -= 1;
            targetCell = Game.map[Y][X];
        } else if (D == Direction.RIGHT) {
            if (X + 1 > 14) {
                throw new MovementException();
            }
            X += 1;
            targetCell = Game.map[Y][X];
        } else {
            if (Y - 1 < 0) {
                throw new MovementException();
            }
            Y -= 1;
            targetCell = Game.map[Y][X];
        }
        if (targetCell instanceof CharacterCell) {
            if (checkCharachterCell(targetCell)) {
                throw new MovementException();
            }
        }
        if (targetCell instanceof CollectibleCell) {
            Collectible collectible = ((CollectibleCell) targetCell).getCollectible();
            collectible.pickup(this);
        }
        if (targetCell instanceof TrapCell) {
            int TrapDamage = ((TrapCell) targetCell).getTrapDamage();
            int newHp = this.getCurrentHp() - TrapDamage;
            if (newHp <= 0) {
                onCharacterDeath();
                return;
            }
            this.setCurrentHp(newHp);
        }
        Point newLocation = new Point(X, Y);
        this.setLocation(newLocation);
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (((j == X) || (j == X + 1) || (j == X - 1)) && ((i == Y) || (i == Y + 1) || (i == Y - 1))) {
                    Game.map[i][j].setVisible(true);
                }
            }
        }
    }

}
