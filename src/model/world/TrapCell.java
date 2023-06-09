package model.world;

public class TrapCell extends Cell {
    private int trapDamage;

    public TrapCell() {
        int[] choices = { 10, 20, 30 };
        int randomNum = (int) (Math.random() * 3);
        this.trapDamage = choices[randomNum];
    }

    public int getTrapDamage() {
        return trapDamage;
    }

}
