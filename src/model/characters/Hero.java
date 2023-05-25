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
        return (!(targetX > X + 1 || targetX < X - 1 || targetY > Y + 1 || targetY < Y - 1));
    }

    @Override
    public void attack() throws InvalidTargetException, NotEnoughActionsException {
        if (this.getCurrentHp() <= 0) {
            this.onCharacterDeath();
            return;
        }
        if (getTarget() == null) {
            throw new InvalidTargetException("You have to select a target!");
        }
        if (!(getTarget() instanceof Zombie)) {
            throw new InvalidTargetException("Cant attack a Hero!");
        }
        if (!(hasValidTarget())) {
            throw new InvalidTargetException("Target out of range!");
        }
        if (!(this instanceof Fighter && isSpecialAction())) {
            if (getActionsAvailable() <= 0) {
                throw new NotEnoughActionsException("Not enough action points!");
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
            throw new NotEnoughActionsException("Not enough action points!");
        }
        CharacterCell prevCell = (CharacterCell) Game.map[X][Y];
        Cell targetCell = null;
        if (D == Direction.UP) {
            if (X + 1 > 14) {
                throw new MovementException("Cant go there!");
            }
            X += 1;
            targetCell = Game.map[X][Y];
        } else if (D == Direction.LEFT) {
            if (Y - 1 < 0) {
                throw new MovementException("Cant go there!");
            }
            Y -= 1;
            targetCell = Game.map[X][Y];
        } else if (D == Direction.RIGHT) {
            if (Y + 1 > 14) {
                throw new MovementException("Cant go there!");
            }
            Y += 1;
            targetCell = Game.map[X][Y];
        } else if (D == Direction.DOWN) {
            if (X - 1 < 0) {
                throw new MovementException("Cant go there!");
            }
            X -= 1;
            targetCell = Game.map[X][Y];
        }
        if (targetCell instanceof CharacterCell) {
            if (((CharacterCell) targetCell).containsCharacter()) {
                throw new MovementException("Cell Occupied!");
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
        if (getTarget() == null) {
            throw new InvalidTargetException("Select a zombie to cure!");
        }
        if (!(this.getTarget() instanceof Zombie)) {
            throw new InvalidTargetException("Cant cure a hero");
        }
        if (this.getActionsAvailable() <= 0) {
            throw new NotEnoughActionsException("Not enough action points!");
        }
        if (this.getVaccineInventory().isEmpty()) {
            throw new NoAvailableResourcesException("No vaccines to use!");
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
            throw new InvalidTargetException("Target out of range");

        }
        Vaccine v = this.getVaccineInventory().get(0);
        v.use(this);
    }
}
