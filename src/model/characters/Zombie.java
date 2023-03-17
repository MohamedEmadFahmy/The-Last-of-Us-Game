package model.characters;

public class Zombie extends Character {
    private static int ZOMBIES_COUNT = 0;
    private int currentHp = 40;
    private int Damage = 10;
    private String Name;
    public Zombie() {
        this.Name = "Zombie" + ++ZOMBIES_COUNT;
    }
}
