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

    @Override
    public void attack() throws InvalidTargetException, NotEnoughActionsException {
        if (!(getTarget() instanceof Zombie)) {
            throw new InvalidTargetException();
        }
        if (!isValidTarget()) {
            throw new InvalidTargetException();
        }
        if (!(this instanceof Fighter && isSpecialAction())) {
            setActionsAvailable(getActionsAvailable() - 1);
        }
        super.attack();
    }

    public abstract void useSpecial() throws InvalidTargetException, NoAvailableResourcesException;

    public void move(Direction D) throws MovementException, NotEnoughActionsException {
        if (this.actionsAvailable < 1) {
            throw new NotEnoughActionsException();
        }
        int X = this.getLocation().x;
        int Y = this.getLocation().y;
        CharacterCell prevCell = (CharacterCell) Game.map[X][Y];
        Cell targetCell = null;
        if (D == Direction.UP) {
            if (X + 1 > 14) {
                throw new MovementException();
            }
            X += 1;
            targetCell = Game.map[X][Y];
        } else if (D == Direction.LEFT) {
            if (Y - 1 < 0) {
                throw new MovementException();
            }
            Y -= 1;
            targetCell = Game.map[X][Y];
        } else if (D == Direction.RIGHT) {
            if (Y + 1 > 14) {
                throw new MovementException();
            }
            Y += 1;
            targetCell = Game.map[X][Y];
        } else if (D == Direction.DOWN) {
            if (X - 1 < 0) {
                throw new MovementException();
            }
            X -= 1;
            targetCell = Game.map[X][Y];
        }
        if (targetCell instanceof CharacterCell) {
            if (((CharacterCell) targetCell).containsCharacter()) {
                throw new MovementException();
            }
            ((CharacterCell) targetCell).setCharacter(this);
        }

        this.setActionsAvailable(this.getActionsAvailable() - 1);
        prevCell.setCharacter(null);
        Point newLocation = new Point(X, Y); // update location of Hero and set previous cell to be empty
        this.setLocation(newLocation);

        if (targetCell instanceof CollectibleCell) {
            Collectible collectible = ((CollectibleCell) targetCell).getCollectible();
            collectible.pickUp(this);
            Game.map[X][Y] = new CharacterCell(this);
        }
        if (targetCell instanceof TrapCell) {
            int TrapDamage = ((TrapCell) targetCell).getTrapDamage();
            targetCell = new CharacterCell(this);
            // Game.map[X][Y] = new CharacterCell(this);
            int newHp = this.getCurrentHp() - TrapDamage;
            this.setCurrentHp(newHp);
            if (newHp <= 0) {
                ((CharacterCell) targetCell).setCharacter(null);
                this.onCharacterDeath();
                return;
            }
        }
        Game.updateVisibility(newLocation);
    }

    public void cure() throws InvalidTargetException, NotEnoughActionsException, NoAvailableResourcesException {
        if (this.getActionsAvailable() <= 0) {
            throw new NotEnoughActionsException();
        }
        if (this.getVaccineInventory().isEmpty()) {
            throw new NoAvailableResourcesException();
        }
        if (!(this.isValidTarget())) {
            throw new InvalidTargetException();
        }
        if (!(this.getTarget() instanceof Zombie)) {
            throw new InvalidTargetException();
        }
        Game.updateVisibility(this.getTarget().getLocation());
        this.setTarget(null);
        this.setActionsAvailable(this.getActionsAvailable() - 1);
        Vaccine v = this.getVaccineInventory().get(0);
        v.use(this);
        Zombie.ZOMBIES_COUNT -= 1;
    }
}
