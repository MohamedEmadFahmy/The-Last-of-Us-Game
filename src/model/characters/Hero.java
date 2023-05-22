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

    public boolean hasValidTarget() {
        int targetX = this.getTarget().getLocation().x;
        int targetY = this.getTarget().getLocation().y;
        int X = this.getLocation().x;
        int Y = this.getLocation().y;
        if (this instanceof Medic && targetX == X && targetY == Y) {
            return true;
        }
        return (!(targetX > X + 1 || targetX < X - 1 || targetY > Y + 1 || targetY < Y - 1));
    }

    @Override
    public void attack() throws InvalidTargetException, NotEnoughActionsException {
        if (this.getCurrentHp() <= 0) {
            this.onCharacterDeath();
            return;
        }
        if (!(getTarget() instanceof Zombie)) {
            throw new InvalidTargetException();
        }
        if (!(hasValidTarget())) {
            throw new InvalidTargetException(
                    "Hero Location: " + this.getLocation() + " Zombie Location: " + this.getTarget().getLocation());
        }
        if (!(this instanceof Fighter && isSpecialAction())) {
            if (getActionsAvailable() <= 0) {
                throw new NotEnoughActionsException();
            }
            setActionsAvailable(getActionsAvailable() - 1);
        }
        super.attack();
    }

    public abstract void useSpecial() throws InvalidTargetException, NoAvailableResourcesException;

    public void move(Direction D) throws MovementException, NotEnoughActionsException {
        int X = this.getLocation().x;
        int Y = this.getLocation().y;
        if (this.getCurrentHp() <= 0) {
            this.onCharacterDeath();
            return;
        }
        if (this.actionsAvailable <= 0) {
            throw new NotEnoughActionsException();
        }
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
        }

        Game.map[X][Y] = new CharacterCell(this);
        this.setActionsAvailable(this.getActionsAvailable() - 1);
        prevCell.setCharacter(null);
        Point newLocation = new Point(X, Y); // update location of Hero and set previous cell to be empty
        this.setLocation(newLocation);

        if (targetCell instanceof CollectibleCell) {
            Collectible collectible = ((CollectibleCell) targetCell).getCollectible();
            collectible.pickUp(this);
        }
        if (targetCell instanceof TrapCell) {
            int TrapDamage = ((TrapCell) targetCell).getTrapDamage();
            int newHp = this.getCurrentHp() - TrapDamage;
            this.setCurrentHp(newHp);
        }
        Game.updateVisibility(newLocation);
    }

    public void cure() throws InvalidTargetException, NotEnoughActionsException, NoAvailableResourcesException {
        if (this.getCurrentHp() <= 0) {
            this.onCharacterDeath();
            return;
        }
        if (this.getActionsAvailable() <= 0) {
            throw new NotEnoughActionsException();
        }
        if (this.getVaccineInventory().isEmpty()) {
            throw new NoAvailableResourcesException();
        }
        if (!(this.getTarget() instanceof Zombie)) {
            throw new InvalidTargetException();
        }
        int targetX = this.getTarget().getLocation().x;
        int targetY = this.getTarget().getLocation().y;
        int X = this.getLocation().x;
        int Y = this.getLocation().y;
        // if (!((targetX <= X + 1 && targetX >= X - 1) && (targetY <= Y + 1 && targetX
        // >= Y - 1))) {
        // System.out.println("Out of range");
        // throw new InvalidTargetException();
        // }
        if (targetX > X + 1 || targetX < X - 1 || targetY > Y + 1 || targetY < Y - 1) {
            System.out.println("Out of range");
            throw new InvalidTargetException();

        }
        Vaccine v = this.getVaccineInventory().get(0);
        v.use(this);
    }
}
